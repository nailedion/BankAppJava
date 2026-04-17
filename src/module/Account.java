package module;

import exception.InsufficientFundsException;
import exception.InvalidDepositException;
import java.util.ArrayList;
import java.util.List;

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

    public void addTransaction(Transaction t) {
        this.transactions.add(t);
    }

    public void deposit(double amount) throws InvalidDepositException {
        if (amount <= 0) {
            throw new InvalidDepositException("Suma pe care doriti sa o depuneti in contul " + iban + " este invalida!");
        }
        this.balance += amount;
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException("Fonduri insuficiente pe contul " + iban + '!');
        }
        this.balance -= amount;
    }

    public String getIban() { return iban; }
    public double getBalance() { return balance; }
    public Currency getCurrency() { return currency; }
    public List<Transaction> getTransactions() { return new ArrayList<>(transactions); }

    public abstract String getAccountType();

    @Override
    public String toString() {
        return String.format("%s [%s] - Sold: %.2f %s", getAccountType(), iban, balance, currency.getSymbol());
    }
}