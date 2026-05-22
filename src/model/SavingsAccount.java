package model;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String iban, double balance, Currency currency, double interestRate) {
        super(iban, balance, currency);
        this.interestRate = interestRate;
    }

    public double getInterestRate() { return interestRate; }

    @Override
    public String getAccountType() {
        return "Cont de Economii(Dobanda: " + interestRate + "%)";
    }
}