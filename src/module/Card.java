package module;

import java.time.LocalDate;

public class Card {
    private String cardNumber;
    private Account linkedAccount;
    private boolean isContactless;
    private LocalDate expiryDate;

    public Card(String cardNumber, Account linkedAccount, boolean isContactless, LocalDate expiryDate) {
        this.cardNumber = cardNumber;
        this.linkedAccount = linkedAccount;
        this.isContactless = isContactless;
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "Cardul cu terminatia " + cardNumber.substring(cardNumber.length() - 4) + " asociat contului " + linkedAccount.getIban();
    }
}