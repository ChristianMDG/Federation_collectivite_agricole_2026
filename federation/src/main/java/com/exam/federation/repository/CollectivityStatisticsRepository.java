package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.dto.CollectivityInformation;
import com.exam.federation.dto.CollectivityOverallStatistics;
import org.springframework.stereotype.Repository;

import java.sql.*;
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

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, number, name, location FROM collectivity");
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
                stat.setNewMembersNumber(countNewMembers(collectivityId, from, to));
                stat.setOverallMemberCurrentDuePercentage(
                        computeCurrentDuePercentage(collectivityId, from, to));
                stats.add(stat);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stats;
    }

    private int countNewMembers(String collectivityId, LocalDate from, LocalDate to) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("""
                    SELECT COUNT(*) FROM member
                    WHERE collectivity_id = ? AND registration_date BETWEEN ? AND ?
                    """);
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private double computeCurrentDuePercentage(String collectivityId, LocalDate from, LocalDate to) {
        int totalMembers = countTotalMembers(collectivityId);
        if (totalMembers == 0) return 0.0;

        List<String> feeIds = new ArrayList<>();
        List<Double> feeAmounts = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("""
                    SELECT id, amount FROM membership_fee
                    WHERE collectivity_id = ? AND status = 'ACTIVE'
                    """);
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                feeIds.add(rs.getString("id"));
                feeAmounts.add(rs.getDouble("amount"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (feeIds.isEmpty()) return 100.0;

        int membersUpToDate = 0;

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id FROM member WHERE collectivity_id = ?");
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String memberId = rs.getString("id");
                boolean upToDate = true;
                for (int i = 0; i < feeIds.size(); i++) {
                    double paid = getPaidAmount(memberId, feeIds.get(i), from, to);
                    if (paid < feeAmounts.get(i)) {
                        upToDate = false;
                        break;
                    }
                }
                if (upToDate) membersUpToDate++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Math.round((double) membersUpToDate / totalMembers * 100.0 * 100.0) / 100.0;
    }

    private int countTotalMembers(String collectivityId) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM member WHERE collectivity_id = ?");
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private double getPaidAmount(String memberId, String feeId, LocalDate from, LocalDate to) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("""
                    SELECT COALESCE(SUM(amount), 0) FROM member_payment
                    WHERE member_id = ? AND membership_fee_id = ?
                    AND creation_date BETWEEN ? AND ?
                    """);
            ps.setString(1, memberId);
            ps.setString(2, feeId);
            ps.setDate(3, Date.valueOf(from));
            ps.setDate(4, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0.0;
    }
}