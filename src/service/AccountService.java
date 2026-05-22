package service;

import exception.AccountNotFoundException;
import exception.CustomerNotFoundException;
import exception.InsufficientFundsException;
import exception.InvalidDepositException;
import model.*;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountService {
    private final AccountRepository accountRepository = AccountRepository.getInstance();
    private final TransactionRepository transactionRepository = TransactionRepository.getInstance();

    private AccountService() {}
    private static class Holder { private static final AccountService INSTANCE = new AccountService(); }
    public static AccountService getInstance() { return Holder.INSTANCE; }

    public void addAccountToCustomer(Integer customerId, Account account) {
        AuditService.getInstance().logAction("addAccountToCustomer");
        try {
            if (accountRepository.findById(account.getIban()).isPresent()) {
                System.out.println("Eroare: IBAN-ul " + account.getIban() + " exista deja in sistem!");
                return;
            }

            CustomerService.getInstance().findCustomerById(customerId);

            accountRepository.saveWithTransaction(account, customerId);
            System.out.println("    Cont salvat in DB (" + account.getIban() + ") pentru clientul ID " + customerId);

        } catch (CustomerNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Eroare critica la scrierea in baza de date: " + e.getMessage());
        }
    }

    public void closeAccount(String iban) {
        AuditService.getInstance().logAction("closeAccount");
        if (accountRepository.findById(iban).isPresent()) {
            accountRepository.delete(iban);
            System.out.println("-> Contul " + iban + " a fost sters din baza de date!");
        } else {
            System.out.println("Eroare: IBAN negasit.");
        }
    }

    public Account findAccountByIban(String iban) throws AccountNotFoundException {
        AuditService.getInstance().logAction("findAccountByIban");
        Optional<Account> acc = accountRepository.findById(iban);
        if (acc.isEmpty()) {
            throw new AccountNotFoundException("Contul cu IBAN-ul " + iban + " nu exista!");
        }
        return acc.get();
    }

    public void deposit(String iban, double amount) {
        AuditService.getInstance().logAction("deposit");
        try {
            Account account = findAccountByIban(iban);
            account.deposit(amount);
            accountRepository.update(account);

            String txId = UUID.randomUUID().toString();
            transactionRepository.saveTransaction(txId, iban, amount, account.getCurrency().name(),
                    "Depunere numerar", TransactionStatus.SUCCESS.name(), TransactionType.DEPOSIT.name(), LocalDateTime.now().toString());

            System.out.println("    Depunere reusita in DB! Sold curent: " + account.getBalance());
        } catch (AccountNotFoundException | InvalidDepositException e) {
            System.out.println("Eroare la depunere: " + e.getMessage());
        }
    }

    public void withdraw(String iban, double amount) {
        AuditService.getInstance().logAction("withdraw");
        Account account = null;
        try {
            account = findAccountByIban(iban);
            account.withdraw(amount);
            accountRepository.update(account);

            String txId = UUID.randomUUID().toString();
            transactionRepository.saveTransaction(txId, iban, amount, account.getCurrency().name(),
                    "Retragere numerar", TransactionStatus.SUCCESS.name(), TransactionType.WITHDRAWAL.name(), LocalDateTime.now().toString());
            System.out.println("    Retragere reusita din DB! Sold curent: " + account.getBalance());

        } catch (InsufficientFundsException e) {
            if (account != null) {
                String txId = UUID.randomUUID().toString();
                transactionRepository.saveTransaction(txId, iban, amount, account.getCurrency().name(),
                        "Retragere esuata: " + e.getMessage(), TransactionStatus.FAILED.name(), TransactionType.WITHDRAWAL.name(), LocalDateTime.now().toString());
            }
            System.out.println("Eroare la retragere: " + e.getMessage());
        } catch (AccountNotFoundException e) {
            System.out.println("Eroare la retragere: " + e.getMessage());
        }
    }

    public void showTransactions(String iban) {
        AuditService.getInstance().logAction("showTransactions");
        try {
            findAccountByIban(iban);
            System.out.println("Extras de cont din DB pentru " + iban + ":");
            List<Transaction> transactions = transactionRepository.findAllByIban(iban);

            if (transactions.isEmpty()) {
                System.out.println("Nicio tranzactie efectuata!");
            } else {
                for (Transaction t : transactions) {
                    System.out.println(t.toString());
                }
            }
        } catch (AccountNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    public void showAdvancedJoinReport() {
        AuditService.getInstance().logAction("showAdvancedJoinReport");
        String sql = "SELECT c.first_name, c.last_name, a.iban, a.balance, a.currency " +
                "FROM customers c " +
                "INNER JOIN accounts a ON c.id = a.customer_id " +
                "ORDER BY c.last_name ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            boolean areData = false;
            while(rs.next()) {
                areData = true;
                System.out.printf("Client: %s %s | IBAN: %s | Sold: %.2f %s\n",
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("iban"), rs.getDouble("balance"), rs.getString("currency"));
            }
            if(!areData) System.out.println("Nu exista conturi asociate in acest moment.");
        } catch (SQLException e) {
            System.out.println("Eroare la rularea raportului JOIN: " + e.getMessage());
        }
    }
}