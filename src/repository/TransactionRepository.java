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
        throw new UnsupportedOperationException("Folositi saveWithAccountIban sau saveTransaction pentru a specifica IBAN-ul.");
    }

    public void saveWithAccountIban(Transaction t, String iban) {
        String sql = "INSERT INTO transactions (id, account_iban, amount, currency, description, status, type, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, t.getTransactionId());
            pstmt.setString(2, iban);
            pstmt.setDouble(3, t.getAmount());
            pstmt.setString(4, t.getCurrency().name());
            pstmt.setString(5, t.getDescription());
            pstmt.setString(6, t.getStatus().name());
            pstmt.setString(7, t.getType().name());
            pstmt.setString(8, t.getTimestamp().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Eroare la salvarea tranzactiei: " + e.getMessage());
        }
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

    @Override
    public Optional<Transaction> findById(String id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Transaction(
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
            System.err.println("Eroare la cautarea tranzactiei: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY timestamp DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
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
        } catch (SQLException e) {
            System.err.println("Eroare la listarea tranzactiilor: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void update(Transaction entity) {
        String sql = "UPDATE transactions SET description = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getDescription());
            pstmt.setString(2, entity.getStatus().name());
            pstmt.setString(3, entity.getTransactionId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Eroare la actualizarea tranzactiei: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Eroare la stergerea tranzactiei: " + e.getMessage());
        }
    }
}