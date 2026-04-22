package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.dto.CreateMembershipFee;
import com.exam.federation.dto.MembershipFee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class MembershipFeeRepository {
    public List<MembershipFee> saveAll(String collectivityId, List<CreateMembershipFee> requests) {

        private final DataSource dataSource;

          p

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

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    MembershipFee fee = new MembershipFee();
                    fee.setId(rs.getString("id"));
                    fee.setEligibleFrom(rs.getDate("eligible_from").toLocalDate());
                    fee.setFrequency(Frequency.valueOf(rs.getString("frequency")));
                    fee.setAmount(rs.getDouble("amount"));
                    fee.setLabel(rs.getString("label"));
                    fee.setStatus(ActivityStatus.valueOf(rs.getString("status")));
                    responses.add(fee);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error creating membership fee: " + e.getMessage());
        }
        return responses;
    }
}
