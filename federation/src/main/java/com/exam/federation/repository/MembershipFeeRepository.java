package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.entity.Enums.ActivityStatus;
import com.exam.federation.entity.Enums.Frequency;
import com.exam.federation.entity.MembershipFee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class MembershipFeeRepository {
    private DataSource dataSource;

    public List<MembershipFee> findByCollectivityId(String collectivityId) {
        List<MembershipFee> membershipFeeList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("""
            SELECT id, collectivity_id, eligible_from, frequency, amount, label, status
                        FROM membership_fee
                        WHERE collectivity_id = ?""");

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
