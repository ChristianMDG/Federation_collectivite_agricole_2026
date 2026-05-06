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

        String sql = """
            SELECT 
                m.id,
                m.firstname,
                m.lastname,
                m.email,
                m.occupation,
                COALESCE(SUM(mp.amount), 0) as earned_amount,
                COALESCE((
                    SELECT SUM(mf.amount)
                    FROM membership_fee mf
                    WHERE mf.collectivity_id = ?
                      AND mf.status = 'ACTIVE'
                      AND mf.eligible_from <= ?
                      AND NOT EXISTS (
                          SELECT 1 
                          FROM member_payment mp2 
                          WHERE mp2.member_id = m.id 
                            AND mp2.membership_fee_id = mf.id
                      )
                ), 0) as unpaid_amount
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

            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(to));
            ps.setDate(3, Date.valueOf(from));
            ps.setDate(4, Date.valueOf(to));
            ps.setString(5, collectivityId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MemberDescription memberDesc = new MemberDescription();
                memberDesc.setId(rs.getString("id"));
                memberDesc.setFirstName(rs.getString("firstname"));
                memberDesc.setLastName(rs.getString("lastname"));
                memberDesc.setEmail(rs.getString("email"));
                memberDesc.setOccupation(rs.getString("occupation"));

                CollectivityLocalStatistics stat = new CollectivityLocalStatistics();
                stat.setMemberDescription(memberDesc);
                stat.setEarnedAmount(rs.getDouble("earned_amount"));
                stat.setUnpaidAmount(rs.getDouble("unpaid_amount"));

                statistics.add(stat);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du calcul des statistiques: " + e.getMessage(), e);
        }

        return statistics;
    }
}