package module;

import exception.InsufficientFundsException;
import exception.InvalidDepositException;

import java.util.*;

public abstract class Account {
    private String iban;
    private double balance;
    private Currency currency;
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        this.transactions.add(t);
    }

    public Account(String iban, double balance, Currency currency) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
    }

    public void deposit(double amount) throws InvalidDepositException {
        if (amount <= 0) {
            throw new InvalidDepositException("Suma pe cere doriti sa o depuneti in contul "+ iban+" este invalida!");
        }
        this.balance += amount;
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException("Fonduri insuficiente pe contul " + iban+'!');
        }
        this.balance -= amount;
    }

    public String getIban() { return iban; }
    public double getBalance() { return balance; }
    public Currency getCurrency() { return currency; }
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
}