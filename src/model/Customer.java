package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Customer implements Comparable<Customer> {
    private Integer id;
    private String firstName, lastName, email;

    private Map<String, Account> accounts = new HashMap<>();

    public Customer(Integer id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void addAccount(Account account) {
        if (!accounts.containsKey(account.getIban())) {
            this.accounts.put(account.getIban(), account);
        } else {
            System.out.println("Eroare: Clientul are deja un cont cu IBAN-ul " + account.getIban());
        }
    }

    public boolean removeAccountByIban(String iban) {
        return accounts.remove(iban) != null;
    }

    public Account getAccountByIban(String iban) {
        return accounts.get(iban);
    }

    public List<Account> getAccountList() {
        return new ArrayList<>(accounts.values());
    }

    public Integer getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public int compareTo(Customer other) {
        int numeComparison = this.lastName.compareToIgnoreCase(other.lastName);
        if (numeComparison != 0) return numeComparison;
        return this.id.compareTo(other.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Client [%d]: %s %s - Email: %s (Conturi: %d)", id, firstName, lastName, email, accounts.size());
    }
}