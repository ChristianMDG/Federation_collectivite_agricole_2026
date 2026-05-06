package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.dto.CollectivityLocalStatistics;
import com.exam.federation.dto.MemberDescription;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatisticsRepository {

    private DataSource dataSource;

    public StatisticsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<CollectivityLocalStatistics> getLocalStatistics(String collectivityId, LocalDate from, LocalDate to) {
        List<CollectivityLocalStatistics> statistics = new ArrayList<>();

        ActiveFee activeFee = getActiveFee(collectivityId, to);
        double activeFeeAmount = 0;
        String activeFeeId = null;

        if (activeFee != null) {
            activeFeeAmount = activeFee.amount;
            activeFeeId = activeFee.id;
        }

        String sql = """
            SELECT 
                m.id,
                m.firstname,
                m.lastname,
                m.email,
                m.occupation,
                COALESCE(SUM(mp.amount), 0) as earned_amount
            FROM member m
            INNER JOIN collectivity_members cm ON cm.member_id = m.id
            LEFT JOIN member_payment mp 
                ON mp.member_id = m.id 
                AND mp.creation_date BETWEEN ? AND ?
            WHERE cm.collectivity_id = ?
            GROUP BY m.id, m.firstname, m.lastname, m.email, m.occupation
            ORDER BY m.firstname, m.lastname
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ps.setString(3, collectivityId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String memberId = rs.getString("id");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String email = rs.getString("email");
                String occupation = rs.getString("occupation");
                double earned = rs.getDouble("earned_amount");

                double paidForActiveFee = getPaidForActiveFee(conn, memberId, activeFeeId);

                boolean isUpToDate = paidForActiveFee >= activeFeeAmount;
                double unpaid = 0;
                if (isUpToDate == false) {
                    unpaid = activeFeeAmount - paidForActiveFee;
                }

                MemberDescription memberDesc = new MemberDescription();
                memberDesc.setId(memberId);
                memberDesc.setFirstName(firstname);
                memberDesc.setLastName(lastname);
                memberDesc.setEmail(email);
                memberDesc.setOccupation(occupation);

                CollectivityLocalStatistics stat = new CollectivityLocalStatistics();
                stat.setMemberDescription(memberDesc);
                stat.setEarnedAmount(earned);
                stat.setUnpaidAmount(unpaid);

                statistics.add(stat);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du calcul des statistiques: " + e.getMessage(), e);
        }

        return statistics;
    }

    private double getPaidForActiveFee(Connection conn, String memberId, String activeFeeId) {
        if (activeFeeId == null) {
            return 0;
        }

        String sql = "SELECT COALESCE(SUM(amount), 0) FROM member_payment WHERE member_id = ? AND membership_fee_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberId);
            ps.setString(2, activeFeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du calcul du paiement: " + e.getMessage(), e);
        }
        return 0;
    }

    private ActiveFee getActiveFee(String collectivityId, LocalDate to) {
        String sql = """
            SELECT id, amount
            FROM membership_fee
            WHERE collectivity_id = ? AND status = 'ACTIVE'
            LIMIT 1
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ActiveFee fee = new ActiveFee();
                fee.id = rs.getString("id");
                fee.amount = rs.getDouble("amount");
                return fee;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la cotisation: " + e.getMessage(), e);
        }
        return null;
    }

    private class ActiveFee {
        String id;
        double amount;
    }
}