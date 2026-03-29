package module;

import java.util.*;

public abstract class Account {
    private String iban;
    private double balance;
    private Currency currency;
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        this.transactions.add(t);
    }
}