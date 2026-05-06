package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.dto.CollectivityLocalStatistics;
import com.exam.federation.dto.MemberDescription;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatisticsRepository {

    private final DataSource dataSource;

    public StatisticsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<CollectivityLocalStatistics> getLocalStatistics(String collectivityId, LocalDate from, LocalDate to) {
        List<CollectivityLocalStatistics> statistics = new ArrayList<>();

        ActiveFee activeFee = getActiveFee(collectivityId, to);
        double activeFeeAmount = activeFee != null ? activeFee.amount : 0;
        String activeFeeId = activeFee != null ? activeFee.id : null;

        String sql = """
            SELECT 
                m.id,
                m.firstname,
                m.lastname,
                m.email,
                m.occupation,
                COALESCE(SUM(mp.amount), 0) as earned_amount,
                COALESCE((
                    SELECT SUM(mp2.amount)
                    FROM member_payment mp2
                    WHERE mp2.member_id = m.id
                      AND mp2.membership_fee_id = ?
                ), 0) as paid_for_active_fee
            FROM member m
            LEFT JOIN member_payment mp 
                ON mp.member_id = m.id 
                AND mp.creation_date BETWEEN ? AND ?
            WHERE m.collectivity_id = ?
            GROUP BY m.id, m.firstname, m.lastname, m.email, m.occupation
            ORDER BY m.firstname, m.lastname
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, activeFeeId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            ps.setString(4, collectivityId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                double earned = rs.getDouble("earned_amount");
                double paidForActiveFee = rs.getDouble("paid_for_active_fee");

                boolean isUpToDate = paidForActiveFee >= activeFeeAmount;
                double unpaid = isUpToDate ? 0 : (activeFeeAmount - paidForActiveFee);

                MemberDescription memberDesc = new MemberDescription();
                memberDesc.setId(rs.getString("id"));
                memberDesc.setFirstName(rs.getString("firstname"));
                memberDesc.setLastName(rs.getString("lastname"));
                memberDesc.setEmail(rs.getString("email"));
                memberDesc.setOccupation(rs.getString("occupation"));

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
            throw new RuntimeException(e);
        }
        return null;
    }

    private static class ActiveFee {
        String id;
        double amount;
    }
}