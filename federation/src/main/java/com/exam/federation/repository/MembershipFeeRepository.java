package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.dto.CreateMembershipFee;
import com.exam.federation.entity.Enums.ActivityStatus;
import com.exam.federation.entity.Enums.Frequency;
import com.exam.federation.entity.MembershipFee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class MembershipFeeRepository {

    private final DataSource dataSource;

    public List<MembershipFee> saveAll(String collectivityId, List<CreateMembershipFee> requests) {

        List<MembershipFee> responses = new ArrayList<>();

        String sql = """
            INSERT INTO membership_fee (
                id, collectivity_id, eligible_from, frequency, amount, label, status
            ) VALUES (
                'mf_' || nextval('membership_fee_id_seq'),
                ?, ?, ?::frequency_type, ?, ?, 'ACTIVE'::activity_status_type
            )
            RETURNING id, eligible_from, frequency, amount, label, status
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (CreateMembershipFee request : requests) {

                ps.setString(1, collectivityId);
                ps.setDate(2, Date.valueOf(request.getEligibleFrom()));
                ps.setString(3, request.getFrequency().name());
                ps.setDouble(4, request.getAmount());
                ps.setString(5, request.getLabel());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        MembershipFee fee = new MembershipFee();
                        fee.setId(rs.getString("id"));
                        fee.setEligibleFrom(rs.getDate("eligible_from").toLocalDate());
                        fee.setFrequency(Frequency.valueOf(rs.getString("frequency")));
                        fee.setAmount(rs.getBigDecimal("amount"));
                        fee.setLabel(rs.getString("label"));
                        fee.setStatus(ActivityStatus.valueOf(rs.getString("status")));
                        fee.setCollectivityId(collectivityId);

                        responses.add(fee);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error creating membership fee: " + e.getMessage(), e);
        }

        return responses;
    }

    public List<MembershipFee> findByCollectivityId(String collectivityId) {

        List<MembershipFee> membershipFeeList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement ps = connection.prepareStatement("""
                SELECT id, collectivity_id, eligible_from, frequency, amount, label, status
                FROM membership_fee
                WHERE collectivity_id = ?
            """);

            ps.setString(1, collectivityId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MembershipFee membershipFee = new MembershipFee();
                membershipFee.setId(rs.getString("id"));
                membershipFee.setCollectivityId(rs.getString("collectivity_id"));
                membershipFee.setEligibleFrom(rs.getDate("eligible_from").toLocalDate());
                membershipFee.setFrequency(Frequency.valueOf(rs.getString("frequency")));
                membershipFee.setAmount(rs.getBigDecimal("amount"));
                membershipFee.setLabel(rs.getString("label"));
                membershipFee.setStatus(ActivityStatus.valueOf(rs.getString("status")));

                membershipFeeList.add(membershipFee);
            }

            return membershipFeeList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}