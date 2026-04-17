package service;

import exception.AccountNotFoundException;
import exception.InsufficientFundsException;
import exception.InvalidDepositException;
import module.*;

import java.time.LocalDateTime;
import java.util.*;

public class BankService {

    private Map<Integer, Customer> customers = new TreeMap<>();
    private int transactionCounter = 1;

    private BankService() {}

    private static class BankServiceHolder {
        private static final BankService INSTANCE = new BankService();
    }

    public static BankService getInstance() {
        return BankServiceHolder.INSTANCE;
    }



    public void addCustomer(Integer id, String firstName, String lastName, String email) {
        Customer c = new Customer(id, firstName, lastName, email);
        customers.put(id, c);
        System.out.println("Client adaugat cu succes: " + firstName + " " + lastName);
    }

    public Customer findCustomerById(Integer id) {
        return customers.get(id);
    }

    public void addAccountToCustomer(Integer customerId, Account account) {
        Customer c = findCustomerById(customerId);
        if (c != null) {
            c.addAccount(account);
            System.out.println("Cont adaugat cu succes la clientul " + c.getFirstName());
        } else {
            System.out.println("Eroare: Clientul nu a fost gasit!");
        }
    }

    public Account findAccountByIban(String iban) throws AccountNotFoundException {
        for (Customer c : customers.values()) {
            for (Account a : c.getAccountList()) {
                if (a.getIban().equals(iban)) return a;
            }
        }
        throw new AccountNotFoundException("Contul cu IBAN-ul " + iban + " nu a fost gasit!");
    }

    public void depositToAccount(String iban, double amount) {
        try {
            Account account = findAccountByIban(iban);
            account.deposit(amount);

            String transactionId = "TRX" + transactionCounter;
            transactionCounter++;

            Transaction t = new Transaction(transactionId, LocalDateTime.now(), amount, account.getCurrency(), "Depunere numerar", TransactionStatus.SUCCESS, TransactionType.DEPOSIT);
            account.addTransaction(t);
            System.out.println("Depunere realizata cu succes! Noul sold: " + account.getBalance());
        } catch (AccountNotFoundException | InvalidDepositException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    public void withdrawFromAccount(String iban, double amount) {
        try {
            Account account = findAccountByIban(iban);
            account.withdraw(amount);

            String transactionId = "TRX" + transactionCounter;
            transactionCounter++;

            Transaction t = new Transaction(transactionId, LocalDateTime.now(), amount, account.getCurrency(), "Retragere numerar", TransactionStatus.SUCCESS, TransactionType.WITHDRAWAL);
            account.addTransaction(t);
            System.out.println("Retragere realizata cu succes! Noul sold: " + account.getBalance());
        } catch (AccountNotFoundException | InsufficientFundsException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    public void showAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("Nu exista clienti in banca.");
            return;
        }
        System.out.println("\n--- Lista Clienti (Sortata dupa ID) ---");
        for (Customer c : customers.values()) {
            System.out.println(c.toString());
        }
    }

    public void showCustomerAccounts(Integer customerId) {
        Customer c = findCustomerById(customerId);
        if (c != null) {
            System.out.println("\n--- Conturile clientului " + c.getFirstName() + " ---");
            if (c.getAccountList().isEmpty()) {
                System.out.println("Clientul nu are conturi active.");
            } else {
                for (Account a : c.getAccountList()) {
                    System.out.println(a.toString());
                }
            }
        } else {
            System.out.println("Clientul nu a fost gasit.");
        }
    }

    public void showAccountTransactions(String iban) {
        try {
            Account account = findAccountByIban(iban);
            System.out.println("\n--- Tranzactii pentru " + iban + " ---");
            if (account.getTransactions().isEmpty()) {
                System.out.println("Nu exista tranzactii pentru acest cont.");
            } else {
                for (Transaction t : account.getTransactions()) {
                    System.out.println(t.toString());
                }
            }
        } catch (AccountNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }
}