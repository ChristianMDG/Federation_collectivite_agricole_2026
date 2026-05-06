package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.dto.CollectivityActivity;
import com.exam.federation.dto.CreateCollectivityActivity;
import com.exam.federation.dto.MonthlyRecurrenceRule;
import com.exam.federation.entity.Enums.ActivityType;
import com.exam.federation.entity.Enums.DayOfWeek;
import com.exam.federation.entity.Enums.MemberOccupation;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ActivityRepository {

    private final DataSource dataSource;

    public ActivityRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<CollectivityActivity> saveAll(String collectivityId, List<CreateCollectivityActivity> requests) {
        List<CollectivityActivity> responses = new ArrayList<>();
        String sql = """
            INSERT INTO activity (
                id, collectivity_id, label, activity_type, member_occupation_concerned,
                recurrence_week_ordinal, recurrence_day_of_week, executive_date
            ) VALUES (
                'act_' || nextval('activity_id_seq'), ?, ?, ?::varchar, ?,
                ?, ?, ?
            )
            RETURNING id, label, activity_type, member_occupation_concerned, 
                      recurrence_week_ordinal, recurrence_day_of_week, executive_date
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (CreateCollectivityActivity request : requests) {
                if (request.getRecurrenceRule() != null && request.getExecutiveDate() != null) {
                    throw new IllegalArgumentException(
                            "Both recurrence rule and executive date cannot be provided at the same time");
                }

                if (request.getRecurrenceRule() == null && request.getExecutiveDate() == null) {
                    throw new IllegalArgumentException(
                            "Either recurrence rule or executive date must be provided");
                }

                ps.setString(1, collectivityId);
                ps.setString(2, request.getLabel());
                ps.setString(3, request.getActivityType().name());

                String occupations = null;
                if (request.getMemberOccupationConcerned() != null && !request.getMemberOccupationConcerned().isEmpty()) {
                    occupations = String.join(",", request.getMemberOccupationConcerned().stream()
                            .map(Enum::name).toList());
                }
                ps.setString(4, occupations);

                if (request.getRecurrenceRule() != null) {
                    ps.setInt(5, request.getRecurrenceRule().getWeekOrdinal());
                    ps.setString(6, request.getRecurrenceRule().getDayOfWeek().name());
                    ps.setNull(7, Types.DATE);
                } else {
                    ps.setNull(5, Types.INTEGER);
                    ps.setNull(6, Types.VARCHAR);
                    ps.setDate(7, Date.valueOf(request.getExecutiveDate()));
                }

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    responses.add(mapActivity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création des activités: " + e.getMessage(), e);
        }
        return responses;
    }

    private CollectivityActivity mapActivity(ResultSet rs) throws SQLException {
        CollectivityActivity activity = new CollectivityActivity();
        activity.setId(rs.getString("id"));
        activity.setLabel(rs.getString("label"));
        activity.setActivityType(ActivityType.valueOf(rs.getString("activity_type")));


        String occupations = rs.getString("member_occupation_concerned");
        if (occupations != null && !occupations.isEmpty()) {
            List<MemberOccupation> occList = new ArrayList<>();
            for (String occ : occupations.split(",")) {
                occList.add(MemberOccupation.valueOf(occ));
            }
            activity.setMemberOccupationConcerned(occList);
        } else {
            activity.setMemberOccupationConcerned(null);
        }

        int weekOrdinal = rs.getInt("recurrence_week_ordinal");
        if (!rs.wasNull()) {
            MonthlyRecurrenceRule rule = new MonthlyRecurrenceRule();
            rule.setWeekOrdinal(weekOrdinal);
            rule.setDayOfWeek(DayOfWeek.valueOf(rs.getString("recurrence_day_of_week")));
            activity.setRecurrenceRule(rule);
        } else {
            activity.setRecurrenceRule(null);
        }

        Date execDate = rs.getDate("executive_date");
        if (execDate != null) {
            activity.setExecutiveDate(execDate.toLocalDate());
        } else {
            activity.setExecutiveDate(null);
        }

        return activity;
    }

    public List<CollectivityActivity> findByCollectivityId(String collectivityId) {
        String sql = """
            SELECT id, label, activity_type, member_occupation_concerned,
                   recurrence_week_ordinal, recurrence_day_of_week, executive_date
            FROM activity
            WHERE collectivity_id = ?
            ORDER BY COALESCE(executive_date, DATE '9999-12-31') ASC
        """;

        List<CollectivityActivity> activities = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                activities.add(mapActivity(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des activités: " + e.getMessage(), e);
        }

        return activities;
    }

    public boolean existsByIdAndCollectivityId(String activityId, String collectivityId) {
        String sql = "SELECT COUNT(id) FROM activity WHERE id = ? AND collectivity_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, activityId);
            ps.setString(2, collectivityId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking activity existence: " + e.getMessage(), e);
        }
        return false;
    }
}