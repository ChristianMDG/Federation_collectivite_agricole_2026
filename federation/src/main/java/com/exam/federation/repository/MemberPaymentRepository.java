package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.dto.CreateMemberPayment;
import com.exam.federation.dto.FinancialAccount;
import com.exam.federation.dto.MemberPayment;
import com.exam.federation.entity.Enums.PaymentMode;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MemberPaymentRepository {

    private final DataSource dataSource;

    public MemberPaymentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<MemberPayment> saveAll(String memberId, List<CreateMemberPayment> requests, String collectivityId) {
        List<MemberPayment> responses = new ArrayList<>();
        LocalDate today = LocalDate.now();

        String paymentSql = """
            INSERT INTO member_payment (
                id, member_id, membership_fee_id, amount, payment_mode, payment_date
            ) VALUES (
                'mp_' || nextval('member_payment_id_seq'),
                ?, ?, ?, ?::payment_mode_type, ?
            )
            RETURNING id, amount, payment_mode, payment_date
        """;

        String transactionSql = """
            INSERT INTO collectivity_transaction (
                id, collectivity_id, member_id, membership_fee_id, amount, payment_mode, transaction_date
            ) VALUES (
                'tr_' || nextval('transaction_id_seq'),
                ?, ?, ?, ?, ?::payment_mode_type, ?
            )
        """;

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            for (CreateMemberPayment request : requests) {
                try (PreparedStatement ps = conn.prepareStatement(paymentSql)) {
                    ps.setString(1, memberId);
                    ps.setString(2, request.getMembershipFeeIdentifier());
                    ps.setDouble(3, request.getAmount());
                    ps.setString(4, request.getPaymentMode().name());
                    ps.setDate(5, Date.valueOf(today));

                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        MemberPayment payment = new MemberPayment();
                        payment.setId(rs.getString("id"));
                        payment.setAmount(rs.getInt("amount"));
                        payment.setPaymentMode(PaymentMode.valueOf(rs.getString("payment_mode")));
                        payment.setCreationDate(rs.getDate("payment_date").toLocalDate());

                        FinancialAccount account = new FinancialAccount();
                        account.setId(request.getAccountCreditedIdentifier());
                        account.setAmount(0.0);
                        payment.setAccountCredited(account);

                        responses.add(payment);
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(transactionSql)) {
                    ps.setString(1, collectivityId);
                    ps.setString(2, memberId);
                    ps.setString(3, request.getMembershipFeeIdentifier());
                    ps.setDouble(4, request.getAmount());
                    ps.setString(5, request.getPaymentMode().name());
                    ps.setDate(6, Date.valueOf(today));
                    ps.executeUpdate();
                }
            }

            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException("Error creating payment: " + e.getMessage());
        }

        return responses;
    }

    public String findCollectivityIdByMemberId(String memberId) {
        String sql = "SELECT collectivity_id FROM member WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("collectivity_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public boolean membershipFeeExists(String id) {
        String sql = "SELECT COUNT(id) FROM membership_fee WHERE id = ? AND status = 'ACTIVE'";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}