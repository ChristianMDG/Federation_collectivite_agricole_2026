package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.dto.CollectivityTransaction;
import com.exam.federation.dto.MemberResponse;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CollectivityTransactionRepository {

    private final DataSource dataSource;
    private final MemberRepository memberRepository;

    public CollectivityTransactionRepository(DataSource dataSource, MemberRepository memberRepository) {
        this.dataSource = dataSource;
        this.memberRepository = memberRepository;
    }
    public List<CollectivityTransaction> findByCollectivityIdAndDateRange(String collectivityId, LocalDate from, LocalDate to) {
        List<CollectivityTransaction> transactions = new ArrayList<>();
        String sql = """
            SELECT id, transaction_date, amount, payment_mode, member_id
            FROM collectivity_transaction
            WHERE collectivity_id = ? 
              AND transaction_date BETWEEN ? AND ?
            ORDER BY transaction_date DESC
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CollectivityTransaction transaction = new CollectivityTransaction();
                transaction.setId(rs.getString("id"));
                transaction.setTransactionDate(rs.getDate("transaction_date").toLocalDate());
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setPaymentMode(rs.getString("payment_mode"));

                String memberId = rs.getString("member_id");
                MemberResponse member = memberRepository.findById(memberId);
                transaction.setMemberDebited(member);

                transactions.add(transaction);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting transactions: " + e.getMessage());
        }
        return transactions;
    }
}