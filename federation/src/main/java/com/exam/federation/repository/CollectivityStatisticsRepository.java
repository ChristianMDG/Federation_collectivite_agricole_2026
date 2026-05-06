package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.dto.CollectivityInformation;
import com.exam.federation.dto.CollectivityOverallStatistics;
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
public class CollectivityStatisticsRepository {

    private DataSource dataSource;

    public CollectivityStatisticsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<CollectivityOverallStatistics> getOverallStatistics(LocalDate from, LocalDate to) {
        List<CollectivityOverallStatistics> stats = new ArrayList<>();

        String sql = "SELECT id, number, name, location FROM collectivity";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String collectivityId = rs.getString("id");

                CollectivityInformation info = new CollectivityInformation();
                info.setId(collectivityId);
                info.setNumber(rs.getString("number"));
                info.setName(rs.getString("name"));
                info.setLocation(rs.getString("location"));

                CollectivityOverallStatistics stat = new CollectivityOverallStatistics();
                stat.setCollectivityInformation(info);
                stat.setNewMembersNumber(countNewMembers(conn, collectivityId, from, to));
                stat.setOverallMemberCurrentDuePercentage(
                        computeCurrentDuePercentage(conn, collectivityId, from, to));
                stats.add(stat);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur calcul statistiques globales: " + e.getMessage(), e);
        }
        return stats;
    }

    private int countNewMembers(Connection conn, String collectivityId, LocalDate from, LocalDate to) {
        String sql = """
            SELECT COUNT(m.id)
            FROM member m
            INNER JOIN collectivity_members cm ON cm.member_id = m.id
            WHERE cm.collectivity_id = ?
            AND m.occupation = 'JUNIOR'
            AND m.registration_date BETWEEN ? AND ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur comptage nouveaux membres: " + e.getMessage(), e);
        }
        return 0;
    }

    private double computeCurrentDuePercentage(Connection conn, String collectivityId, LocalDate from, LocalDate to) {
        int totalMembers = countTotalMembers(conn, collectivityId);
        if (totalMembers == 0) {
            return 0.0;
        }

        String feeSql = """
            SELECT id, amount 
            FROM membership_fee 
            WHERE collectivity_id = ? AND status = 'ACTIVE' AND frequency = 'ANNUALLY'
        """;

        List<String> feeIds = new ArrayList<>();
        List<Double> feeAmounts = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(feeSql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                feeIds.add(rs.getString("id"));
                feeAmounts.add(rs.getDouble("amount"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur récupération cotisations: " + e.getMessage(), e);
        }

        if (feeIds.isEmpty()) {
            return 100.0;
        }

        int membersUpToDate = 0;

        String memberSql = """
            SELECT m.id
            FROM member m
            INNER JOIN collectivity_members cm ON cm.member_id = m.id
            WHERE cm.collectivity_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(memberSql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String memberId = rs.getString("id");
                boolean upToDate = true;
                for (int i = 0; i < feeIds.size(); i++) {
                    double paid = getPaidAmount(conn, memberId, feeIds.get(i), from, to);
                    if (paid < feeAmounts.get(i)) {
                        upToDate = false;
                        break;
                    }
                }
                if (upToDate) {
                    membersUpToDate++;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur calcul membres à jour: " + e.getMessage(), e);
        }

        double percentage = (double) membersUpToDate / totalMembers * 100.0;
        return Math.round(percentage * 100.0) / 100.0;
    }

    private int countTotalMembers(Connection conn, String collectivityId) {
        String sql = """
            SELECT COUNT(m.id)
            FROM member m
            INNER JOIN collectivity_members cm ON cm.member_id = m.id
            WHERE cm.collectivity_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur comptage membres: " + e.getMessage(), e);
        }
        return 0;
    }

    private double getPaidAmount(Connection conn, String memberId, String feeId, LocalDate from, LocalDate to) {
        String sql = """
            SELECT COALESCE(SUM(amount), 0) 
            FROM member_payment 
            WHERE member_id = ? 
            AND membership_fee_id = ?
            AND creation_date BETWEEN ? AND ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberId);
            ps.setString(2, feeId);
            ps.setDate(3, Date.valueOf(from));
            ps.setDate(4, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur calcul paiement: " + e.getMessage(), e);
        }
        return 0.0;
    }
}