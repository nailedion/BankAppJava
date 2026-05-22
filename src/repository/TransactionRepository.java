package repository;

import model.*;
import util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionRepository implements Repository<Transaction, String> {
    private static class Holder { private static final TransactionRepository INSTANCE = new TransactionRepository(); }
    public static TransactionRepository getInstance() { return Holder.INSTANCE; }
    private TransactionRepository() {}

    @Override
    public Transaction save(Transaction entity) {
        throw new UnsupportedOperationException("Folositi saveWithAccountIban");
    }

    public void saveWithAccountIban(Transaction t, String iban) {
        String sql = "INSERT INTO transactions (id, account_iban, amount, currency, description, status, type, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, t.toString().split("\\[")[1].split("\\]")[0]);
        } catch (Exception e) {}
    }

    public void saveTransaction(String id, String iban, double amount, String currency, String desc, String status, String type, String timestamp) {
        String sql = "INSERT INTO transactions (id, account_iban, amount, currency, description, status, type, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, iban);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, currency);
            pstmt.setString(5, desc);
            pstmt.setString(6, status);
            pstmt.setString(7, type);
            pstmt.setString(8, timestamp);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Eroare la salvarea tranzactiei: " + e.getMessage());
        }
    }

    public List<Transaction> findAllByIban(String iban) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_iban = ? ORDER BY timestamp DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, iban);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Transaction(
                            rs.getString("id"),
                            LocalDateTime.parse(rs.getString("timestamp")),
                            rs.getDouble("amount"),
                            Currency.valueOf(rs.getString("currency")),
                            rs.getString("description"),
                            TransactionStatus.valueOf(rs.getString("status")),
                            TransactionType.valueOf(rs.getString("type"))
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la incarcarea tranzactiilor: " + e.getMessage());
        }
        return list;
    }

    @Override public Optional<Transaction> findById(String id) { return Optional.empty(); }
    @Override public List<Transaction> findAll() { return new ArrayList<>(); }
    @Override public void update(Transaction entity) {}
    @Override public void delete(String id) {}
}