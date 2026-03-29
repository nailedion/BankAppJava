package module;

import java.util.*;

public class Customer{
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



    public List<Account> getAccountList() {
        return new ArrayList<>(accountList);
    }

    public String getFirstName(){
        return new String(this.firstName);
    }

    public String getLastName(){
        return new String(this.firstName);
    }

    public Integer getId() {
        return new Integer(id);
    }



    //si setter pt email
}