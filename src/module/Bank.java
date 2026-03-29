package module;

import exception.AccountNotFoundException;
import java.util.*;

public class Bank {
    private List<Customer> customers;

    private Bank() {
        this.customers = new ArrayList<>();
    }

    private static class BankHolder {
        private static final Bank INSTANCE = new Bank();
    }

    public static Bank getInstance() {
        return BankHolder.INSTANCE;
    }



    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public Account findAccountByIban(String iban) throws AccountNotFoundException {
        for (Customer c : customers)
            for (Account a : c.getAccountList())
                if (a.getIban().equals(iban))
                    return a;

        throw new AccountNotFoundException("Contul cu IBAN-ul " + iban + " nu a fost gasit!");
    }



    public List<Customer> getCustomers() {
        return new ArrayList<>(this.customers);
    }
}