package module;

import java.util.ArrayList;
import java.util.List;

public class Customer implements Comparable<Customer> {
    private Integer id;
    private String firstName, lastName, email;
    private List<Account> accountList = new ArrayList<>();

    public Customer(Integer id, String firstName, String lastName, String email) {
        this.id = id; //sa verific daca exista deja si sa arunc o exceptie eventual
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void addAccount(Account account) {
        this.accountList.add(account);
    }

    //ar merge si un delete


    public List<Account> getAccountList() { return new ArrayList<>(accountList); }
    public Integer getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; } // Bug fixat
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public int compareTo(Customer other) {
        return this.id.compareTo(other.id);
    }

    @Override
    public String toString() {
        return String.format("Client [%d]: %s %s - Email: %s (Conturi: %d)", id, firstName, lastName, email, accountList.size());
    }
}