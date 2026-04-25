package model;

import java.time.LocalDateTime;

public final class Transaction implements Comparable<Transaction> {
    private final String transactionId;
    private final String description;
    private final LocalDateTime timestamp;
    private final double amount;
    private final Currency currency;
    private final TransactionStatus status;
    private final TransactionType type;

    public Transaction(String transactionId, LocalDateTime timestamp, double amount, Currency currency, String description, TransactionStatus status, TransactionType type) {
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    @Override
    public int compareTo(Transaction other) {
        return other.timestamp.compareTo(this.timestamp);
    }

    @Override
    public String toString() {
        return String.format("Tranzactie [%s] - %s: %.2f %s | Status: %s | Data: %s",
                transactionId, type, amount, currency.getSymbol(), status, timestamp);
    }
}