package repository;

import model.Account;
import model.Card;
import util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepository implements Repository<Card, String> {
    private static class Holder { private static final CardRepository INSTANCE = new CardRepository(); }
    public static CardRepository getInstance() { return Holder.INSTANCE; }
    private CardRepository() {}

    @Override
    public Card save(Card card) {
        String sql = "INSERT INTO cards (card_number, account_iban, is_contactless, expiry_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, card.getCardNumber());
            pstmt.setString(2, card.getLinkedAccount().getIban());
            pstmt.setInt(3, card.isContactless() ? 1 : 0);
            pstmt.setString(4, card.getExpiryDate().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Eroare la salvarea cardului: " + e.getMessage());
        }
        return card;
    }

    @Override
    public Optional<Card> findById(String id) {
        String sql = "SELECT * FROM cards WHERE card_number = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String iban = rs.getString("account_iban");
                    Account linkedAccount = AccountRepository.getInstance().findById(iban).orElse(null);

                    if (linkedAccount != null) {
                        return Optional.of(new Card(
                                rs.getString("card_number"),
                                linkedAccount,
                                rs.getInt("is_contactless") == 1,
                                LocalDate.parse(rs.getString("expiry_date"))
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la cautarea cardului: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Card> findAll() {
        List<Card> list = new ArrayList<>();
        String sql = "SELECT * FROM cards";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String iban = rs.getString("account_iban");
                Account linkedAccount = AccountRepository.getInstance().findById(iban).orElse(null);

                if (linkedAccount != null) {
                    list.add(new Card(
                            rs.getString("card_number"),
                            linkedAccount,
                            rs.getInt("is_contactless") == 1,
                            LocalDate.parse(rs.getString("expiry_date"))
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la listarea cardurilor: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void update(Card card) {
        String sql = "UPDATE cards SET is_contactless = ?, expiry_date = ? WHERE card_number = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, card.isContactless() ? 1 : 0);
            pstmt.setString(2, card.getExpiryDate().toString());
            pstmt.setString(3, card.getCardNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Eroare la actualizarea cardului: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM cards WHERE card_number = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Eroare la stergerea cardului: " + e.getMessage());
        }
    }
}