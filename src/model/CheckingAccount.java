package model;

import exception.InsufficientFundsException;

public class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(String iban, double balance, Currency currency, double overdraftLimit) {
        super(iban, balance, currency);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > (balance + overdraftLimit)) {
            throw new InsufficientFundsException("Limita de overdraft depasita pentru contul " + iban + '!');
        }
        this.balance -= amount;
    }

    @Override
    public String getAccountType() {
        return "Cont Curent (Overdraft limit: " + overdraftLimit + ")";
    }
}