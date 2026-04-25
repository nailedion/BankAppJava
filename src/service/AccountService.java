package service;

import exception.AccountNotFoundException;
import exception.CustomerNotFoundException;
import exception.InsufficientFundsException;
import exception.InvalidDepositException;
import model.*;

import java.time.LocalDateTime;
import java.util.List;

public class AccountService {
    private int transactionCounter = 1;

    private AccountService() {}
    private static class Holder { private static final AccountService INSTANCE = new AccountService(); }
    public static AccountService getInstance() { return Holder.INSTANCE; }

    public void addAccountToCustomer(Integer customerId, Account account) {
        try {
            try {
                findAccountByIban(account.getIban());
                System.out.println("Eroare: IBAN-ul " + account.getIban() + " exista deja in sistem!");
                return;
            } catch (AccountNotFoundException e) {
            }

            Customer c = CustomerService.getInstance().findCustomerById(customerId);

            c.addAccount(account);
            System.out.println("    Cont creat cu succes (" + account.getIban() + ") pentru " + c.getFirstName());

        } catch (CustomerNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    public void closeAccount(String iban) {
        boolean gasit = false;
        for (Customer c : CustomerService.getInstance().getAllCustomers()) {
            if (c.removeAccountByIban(iban)) {
                gasit = true;
                System.out.println("-> Contul " + iban + " a fost sters!");
                break;
            }
        }

        if(!gasit) System.out.println("Eroare: IBAN negasit.");
    }

    public Account findAccountByIban(String iban) throws AccountNotFoundException {
        for (Customer c : CustomerService.getInstance().getAllCustomers()) {
            Account foundAccount = c.getAccountByIban(iban);

            if (foundAccount != null) {
                return foundAccount;
            }
        }
        throw new AccountNotFoundException("Contul cu IBAN-ul " + iban + " nu exista!");
    }

    public void deposit(String iban, double amount) {
        try {
            Account account = findAccountByIban(iban);
            account.deposit(amount);
            Transaction t = new Transaction("TRX" + (transactionCounter++), LocalDateTime.now(), amount, account.getCurrency(), "Depunere numerar", TransactionStatus.SUCCESS, TransactionType.DEPOSIT);
            account.addTransaction(t);
            System.out.println("    Depunere reusita! Sold curent: " + account.getBalance());
        } catch (AccountNotFoundException | InvalidDepositException e) {
            System.out.println("Eroare la depunere: " + e.getMessage());
        }
    }

    public void withdraw(String iban, double amount) {
        Account account = null;

        try {
            account = findAccountByIban(iban);
            account.withdraw(amount);

            Transaction t = new Transaction("TRX" + (transactionCounter++), LocalDateTime.now(), amount, account.getCurrency(), "Retragere numerar", TransactionStatus.SUCCESS, TransactionType.WITHDRAWAL);
            account.addTransaction(t);
            System.out.println("    Retragere reusita! Sold curent: " + account.getBalance());

        } catch (InsufficientFundsException e) {
            if (account != null) {
                Transaction failedTx = new Transaction("TRX" + (transactionCounter++), LocalDateTime.now(), amount, account.getCurrency(), "Retragere esuata: " + e.getMessage(), TransactionStatus.FAILED, TransactionType.WITHDRAWAL);
                account.addTransaction(failedTx);
            }
            System.out.println("Eroare la retragere: " + e.getMessage());

        } catch (AccountNotFoundException e) {
            System.out.println("Eroare la retragere: " + e.getMessage());
        }
    }

    public void showTransactions(String iban) {
        try {
            Account account = findAccountByIban(iban);
            System.out.println("Extras de cont pentru " + iban + ".");

            List<Transaction> istoriculTranzactiilor = account.getTransactions();

            if (istoriculTranzactiilor.isEmpty()) {
                System.out.println("Nicio tranzactie efectuata!");
            } else {
                istoriculTranzactiilor.sort(null);

                for (Transaction t : istoriculTranzactiilor) {
                    System.out.println(t.toString());
                }
            }
        } catch (AccountNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }
}