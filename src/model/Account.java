package model;

import exception.InsufficientFundsException;
import exception.InvalidDepositException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Account {
    protected String iban;
    protected double balance;
    protected Currency currency;
    protected List<Transaction> transactions = new ArrayList<>();

    public Account(String iban, double balance, Currency currency) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
    }

    public void addTransaction(Transaction t) { this.transactions.add(t); }

    public void deposit(double amount) throws InvalidDepositException {
        if (amount <= 0) throw new InvalidDepositException("Suma de depus e invalida!");
        this.balance += amount;
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) throw new InsufficientFundsException("Fonduri insuficiente pentru IBAN: " + iban);
        this.balance -= amount;
    }

    public String getIban() { return iban; }
    public double getBalance() { return balance; }
    public Currency getCurrency() { return currency; }
    public List<Transaction> getTransactions() { return new ArrayList<>(transactions); }

    public abstract String getAccountType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(iban, account.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban);
    }

    @Override
    public String toString() {
        return String.format("%s [%s] - Sold: %.2f %s", getAccountType(), iban, balance, currency.getSymbol());
    }
}