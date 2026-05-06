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
            WITH active_fee AS (
               
                SELECT id, amount
                FROM membership_fee
                WHERE collectivity_id = ?
                  AND status = 'ACTIVE'
                LIMIT 1
            ),
            total_activities AS (
                
                SELECT COUNT(id) as total
                FROM activity
                WHERE collectivity_id = ?
                  AND (executive_date BETWEEN ? AND ?
                       OR (recurrence_week_ordinal IS NOT NULL AND executive_date IS NULL))
            ),
            member_attendance AS (
                
                SELECT 
                    ma.member_id,
                    COUNT(CASE WHEN ma.attendance_status = 'ATTENDED' THEN 1 END) as attended_count
                FROM member_attendance ma
                JOIN activity a ON a.id = ma.activity_id
                WHERE a.collectivity_id = ?
                  AND (a.executive_date BETWEEN ? AND ?
                       OR (a.recurrence_week_ordinal IS NOT NULL AND a.executive_date IS NULL))
                GROUP BY ma.member_id
            ),
            member_earnings AS (
               
                SELECT 
                    m.id,
                    COALESCE(SUM(mp.amount), 0) as earned_amount
                FROM member m
                LEFT JOIN member_payment mp 
                    ON mp.member_id = m.id 
                    AND mp.creation_date BETWEEN ? AND ?
                WHERE m.collectivity_id = ?
                GROUP BY m.id
            ),
            member_paid_fee AS (
                -- 5. Montant payé pour la cotisation active
                SELECT 
                    mp.member_id,
                    COALESCE(SUM(mp.amount), 0) as paid_amount
                FROM member_payment mp
                WHERE mp.membership_fee_id = (SELECT id FROM active_fee)
                GROUP BY mp.member_id
            )
            SELECT 
                m.id,
                m.firstname,
                m.lastname,
                m.email,
                m.occupation,
                COALESCE(me.earned_amount, 0) as earned_amount,
                CASE 
                    WHEN af.id IS NULL THEN 0
                    WHEN COALESCE(mpf.paid_amount, 0) >= af.amount THEN 0
                    ELSE (af.amount - COALESCE(mpf.paid_amount, 0))
                END as unpaid_amount,
                CASE 
                    WHEN ta.total IS NULL OR ta.total = 0 THEN 0
                    ELSE ROUND((COALESCE(ma.attended_count, 0) * 100.0) / ta.total, 2)
                END as assiduity_percentage
            FROM member m
            CROSS JOIN active_fee af
            CROSS JOIN total_activities ta
            LEFT JOIN member_earnings me ON me.id = m.id
            LEFT JOIN member_paid_fee mpf ON mpf.member_id = m.id
            LEFT JOIN member_attendance ma ON ma.member_id = m.id
            WHERE m.collectivity_id = ?
            ORDER BY m.firstname, m.lastname
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ps.setString(2, collectivityId);
            ps.setDate(3, Date.valueOf(from));
            ps.setDate(4, Date.valueOf(to));
            ps.setString(5, collectivityId);
            ps.setDate(6, Date.valueOf(from));
            ps.setDate(7, Date.valueOf(to));
            ps.setDate(8, Date.valueOf(from));
            ps.setDate(9, Date.valueOf(to));
            ps.setString(10, collectivityId);
            ps.setString(11, collectivityId);

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
                stat.setAssiduityPercentage(rs.getDouble("assiduity_percentage"));

                statistics.add(stat);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du calcul des statistiques: " + e.getMessage(), e);
        }

        return statistics;
    }
}