package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.dto.*;
import com.exam.federation.entity.Enums.MobileBankingService;
import com.exam.federation.entity.Enums.Bank;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FinancialAccountRepository {

    private final DataSource dataSource;

    public FinancialAccountRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public FinancialAccount findById(String id) {
        String sql = "SELECT id, amount FROM financial_account WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String accountId = rs.getString("id");
                Integer amount = rs.getInt("amount");

                // Vérifier le type de compte
                if (isCashAccount(accountId)) {
                    CashAccount account = new CashAccount();
                    account.setId(accountId);
                    account.setAmount(amount);
                    return account;
                } else if (isMobileBankingAccount(accountId)) {
                    return getMobileBankingAccount(accountId, amount);
                } else if (isBankAccount(accountId)) {
                    return getBankAccount(accountId, amount);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private boolean isCashAccount(String id) {
        String sql = "SELECT COUNT(id) FROM cash_account WHERE id = ?";
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

    private boolean isMobileBankingAccount(String id) {
        String sql = "SELECT COUNT(id) FROM mobile_banking_account WHERE id = ?";
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

    private boolean isBankAccount(String id) {
        String sql = "SELECT COUNT(id) FROM bank_account WHERE id = ?";
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

    private MobileBankingAccount getMobileBankingAccount(String id, Integer amount) {
        String sql = "SELECT holder_name, mobile_banking_service, mobile_number FROM mobile_banking_account WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                MobileBankingAccount account = new MobileBankingAccount();
                account.setId(id);
                account.setAmount(amount);
                account.setHolderName(rs.getString("holder_name"));
                account.setMobileBankingService(rs.getString("mobile_banking_service"));
                account.setMobileNumber(rs.getInt("mobile_number"));
                return account;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private BankAccount getBankAccount(String id, Integer amount) {
        String sql = "SELECT holder_name, bank_name, bank_code, bank_branch_code, bank_account_number, bank_account_key FROM bank_account WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                BankAccount account = new BankAccount();
                account.setId(id);
                account.setAmount(amount);
                account.setHolderName(rs.getString("holder_name"));
                account.setBankName(rs.getString("bank_name"));
                account.setBankCode(rs.getInt("bank_code"));
                account.setBankBranchCode(rs.getInt("bank_branch_code"));
                account.setBankAccountNumber(rs.getInt("bank_account_number"));
                account.setBankAccountKey(rs.getInt("bank_account_key"));
                return account;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateAmount(String accountId, Integer amount) {
        String sql = "UPDATE financial_account SET amount = amount + ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, amount);
            ps.setString(2, accountId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}