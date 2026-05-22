package repository;

import model.*;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepository implements Repository<Account, String> {
    private static class Holder { private static final AccountRepository INSTANCE = new AccountRepository(); }
    public static AccountRepository getInstance() { return Holder.INSTANCE; }
    private AccountRepository() {}

    @Override
    public Account save(Account entity) {
        throw new UnsupportedOperationException("Folositi metoda saveWithTransaction(account, customerId)");
    }

    public void saveWithTransaction(Account account, int customerId) throws SQLException {
        String sqlBase = "INSERT INTO accounts (iban, customer_id, balance, currency) VALUES (?, ?, ?, ?)";
        String sqlSub = (account instanceof SavingsAccount) ?
                "INSERT INTO savings_accounts (account_iban, interest_rate) VALUES (?, ?)" :
                "INSERT INTO checking_accounts (account_iban, overdraft_limit) VALUES (?, ?)";

        Connection conn = DatabaseConnection.getInstance().getConnection();

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtBase = conn.prepareStatement(sqlBase)) {
                stmtBase.setString(1, account.getIban());
                stmtBase.setInt(2, customerId);
                stmtBase.setDouble(3, account.getBalance());
                stmtBase.setString(4, account.getCurrency().name());
                stmtBase.executeUpdate();
            }

            try (PreparedStatement stmtSub = conn.prepareStatement(sqlSub)) {
                stmtSub.setString(1, account.getIban());
                if (account instanceof SavingsAccount) {
                    stmtSub.setDouble(2, ((SavingsAccount) account).getInterestRate());
                } else {
                    stmtSub.setDouble(2, ((CheckingAccount) account).getOverdraftLimit());
                }
                stmtSub.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public Optional<Account> findById(String iban) {
        String sql = "SELECT a.*, s.interest_rate, c.overdraft_limit " +
                "FROM accounts a " +
                "LEFT JOIN savings_accounts s ON a.iban = s.account_iban " +
                "LEFT JOIN checking_accounts c ON a.iban = c.account_iban " +
                "WHERE a.iban = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, iban);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double balance = rs.getDouble("balance");
                    Currency currency = Currency.valueOf(rs.getString("currency"));

                    if (rs.getObject("interest_rate") != null) {
                        return Optional.of(new SavingsAccount(iban, balance, currency, rs.getDouble("interest_rate")));
                    } else {
                        return Optional.of(new CheckingAccount(iban, balance, currency, rs.getDouble("overdraft_limit")));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la cautarea contului: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Account> findAllByCustomerId(int customerId) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT a.*, s.interest_rate, c.overdraft_limit " +
                "FROM accounts a " +
                "LEFT JOIN savings_accounts s ON a.iban = s.account_iban " +
                "LEFT JOIN checking_accounts c ON a.iban = c.account_iban " +
                "WHERE a.customer_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String iban = rs.getString("iban");
                    double balance = rs.getDouble("balance");
                    Currency currency = Currency.valueOf(rs.getString("currency"));

                    if (rs.getObject("interest_rate") != null) {
                        list.add(new SavingsAccount(iban, balance, currency, rs.getDouble("interest_rate")));
                    } else {
                        list.add(new CheckingAccount(iban, balance, currency, rs.getDouble("overdraft_limit")));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la incarcarea conturilor clientului: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Account> findAll() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT a.*, s.interest_rate, c.overdraft_limit " +
                "FROM accounts a " +
                "LEFT JOIN savings_accounts s ON a.iban = s.account_iban " +
                "LEFT JOIN checking_accounts c ON a.iban = c.account_iban";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String iban = rs.getString("iban");
                double balance = rs.getDouble("balance");
                Currency currency = Currency.valueOf(rs.getString("currency"));

                if (rs.getObject("interest_rate") != null) {
                    list.add(new SavingsAccount(iban, balance, currency, rs.getDouble("interest_rate")));
                } else {
                    list.add(new CheckingAccount(iban, balance, currency, rs.getDouble("overdraft_limit")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la listarea tuturor conturilor: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void update(Account account) {
        String sql = "UPDATE accounts SET balance = ? WHERE iban = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, account.getBalance());
            pstmt.setString(2, account.getIban());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Eroare la actualizarea soldului: " + e.getMessage());
        }
    }

    @Override
    public void delete(String iban) {
        String sql = "DELETE FROM accounts WHERE iban = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, iban);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Eroare la stergerea contului: " + e.getMessage());
        }
    }
}