package model;

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

    public String getCardNumber() { return cardNumber; }
    public Account getLinkedAccount() { return linkedAccount; }
    public boolean isContactless() { return isContactless; }
    public LocalDate getExpiryDate() { return expiryDate; }

    @Override
    public String toString() {
        return "Cardul cu terminatia " + cardNumber.substring(cardNumber.length() - 4) + " asociat contului " + linkedAccount.getIban();
    }
}