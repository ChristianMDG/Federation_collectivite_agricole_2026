package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.dto.CreateMemberPayment;
import com.exam.federation.dto.MemberPayment;
import com.exam.federation.entity.Enums.PaymentMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MemberPaymentRepository {

    private final DataSource dataSource;

    public MemberPaymentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<MemberPayment> createPayments(String memberId, List<CreateMemberPayment> requests) {

        List<MemberPayment> responses = new ArrayList<>();

        String paymentSql = """
            INSERT INTO member_payment
            (member_id, amount, payment_mode, membership_fee_id, account_credited_id, creation_date)
            VALUES (?, ?, ?, ?, ?, CURRENT_DATE)
            RETURNING id, amount, payment_mode, account_credited_id, creation_date
        """;

        String transactionSql = """
            INSERT INTO collectivity_transaction
            (creation_date, amount, payment_mode, account_credited_id, member_id)
            VALUES (CURRENT_DATE, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement psPayment = conn.prepareStatement(paymentSql);
             PreparedStatement psTransaction = conn.prepareStatement(transactionSql)) {

            for (CreateMemberPayment req : requests) {

                psPayment.setString(1, memberId);
                psPayment.setBigDecimal(2, req.getAmount());
                psPayment.setString(3, req.getPaymentMode().name());
                psPayment.setString(4, req.getMembershipFeeIdentifier());
                psPayment.setString(5, req.getAccountCreditedIdentifier());

                ResultSet rs = psPayment.executeQuery();

                if (rs.next()) {
                    MemberPayment payment = new MemberPayment();
                    payment.setId(rs.getString("id"));
                    payment.setAmount(rs.getBigDecimal("amount"));
                    payment.setPaymentMode(PaymentMode.valueOf(rs.getString("payment_mode")));
                    payment.setAccountCreditedIdentifier(rs.getString("account_credited_id"));
                    payment.setCreationDate(rs.getDate("creation_date").toLocalDate());

                    responses.add(payment);
                }

                psTransaction.setBigDecimal(1, req.getAmount());
                psTransaction.setString(2, req.getPaymentMode().name());
                psTransaction.setString(3, req.getAccountCreditedIdentifier());
                psTransaction.setString(4, memberId);

                psTransaction.executeUpdate();
            }

        } catch (Exception e) {
            throw new RuntimeException("Error creating payments", e);
        }

        return responses;
    }
}