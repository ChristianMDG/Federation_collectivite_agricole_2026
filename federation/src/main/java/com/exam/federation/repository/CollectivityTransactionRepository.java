package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.dto.CollectivityTransaction;
import com.exam.federation.dto.FinancialAccount;
import com.exam.federation.dto.MemberResponse;
import com.exam.federation.entity.Enums.PaymentMode;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CollectivityTransactionRepository {

    private final DataSource dataSource;
    private final MemberRepository memberRepository;
    private final FinancialAccountRepository financialAccountRepository;

    public CollectivityTransactionRepository(DataSource dataSource,
                                             MemberRepository memberRepository,
                                             FinancialAccountRepository financialAccountRepository) {
        this.dataSource = dataSource;
        this.memberRepository = memberRepository;
        this.financialAccountRepository = financialAccountRepository;
    }
    public List<CollectivityTransaction> findByCollectivityIdAndDateRange(String collectivityId, LocalDate from, LocalDate to) {
        List<CollectivityTransaction> transactions = new ArrayList<>();
        String sql = """
            SELECT ct.id, ct.creation_date, ct.amount, ct.payment_mode, ct.member_id, ct.account_credited_id
            FROM collectivity_transaction ct
            WHERE ct.collectivity_id = ? 
              AND ct.creation_date BETWEEN ? AND ?
            ORDER BY ct.creation_date DESC
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
                transaction.setCreationDate(rs.getDate("creation_date").toLocalDate());  // ✅ creationDate
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setPaymentMode(PaymentMode.valueOf(rs.getString("payment_mode")));

                // Récupérer le membre
                String memberId = rs.getString("member_id");
                MemberResponse member = memberRepository.findById(memberId);
                transaction.setMemberDebited(member);

                // Récupérer le compte financier crédité
                String accountId = rs.getString("account_credited_id");
                FinancialAccount account = financialAccountRepository.findById(accountId);
                transaction.setAccountCredited(account);

                transactions.add(transaction);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting transactions: " + e.getMessage(), e);
        }
        return transactions;
    }
}