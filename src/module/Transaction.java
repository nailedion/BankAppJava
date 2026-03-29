package module;

import java.time.LocalDateTime;

public class Transaction {
    private final String transactionId, description;
    private final LocalDateTime timestamp;
    private final double amount;
    private final Currency currency;
    private final TransactionStatus status;

    public Transaction(String transactionId, LocalDateTime timestamp, double amount, Currency currency, String description, TransactionStatus status){
        this.transactionId=transactionId;
        this.timestamp=timestamp;
        this.amount=amount;
        this.currency=currency;
        this.description=description;
        this.status=status;
    }
}