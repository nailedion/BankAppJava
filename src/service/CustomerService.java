package service;

import exception.CustomerNotFoundException;
import model.Customer;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {
    private Map<Integer, Customer> customers = new TreeMap<>();

    private CustomerService() {}
    private static class Holder { private static final CustomerService INSTANCE = new CustomerService(); }
    public static CustomerService getInstance() { return Holder.INSTANCE; }

    public void addCustomer(Integer id, String firstName, String lastName, String email) {
        if(customers.containsKey(id)) {
            System.out.println("Eroare: Clientul cu ID " + id + " exista deja!");
            return;
        }
        customers.put(id, new Customer(id, firstName, lastName, email));
        System.out.println("-> Client adaugat: " + firstName + " " + lastName);
    }

    public Customer findCustomerById(Integer id) throws CustomerNotFoundException {
        if (!customers.containsKey(id)) {
            throw new CustomerNotFoundException("Clientul cu ID " + id + " nu a fost gasit!");
        }
        return customers.get(id);
    }

    public void deleteCustomer(Integer id) {
        if (customers.remove(id) != null) {
            System.out.println("-> Clientul cu ID " + id + " a fost sters din sistem.");
        } else {
            System.out.println("Eroare: Clientul nu a putut fi sters deoarece nu exista.");
        }
    }

    public void showAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("Nu exista clienti in sistem.");
            return;
        }
        System.out.println("Lista Clienti(Sortata dupa ID)");
        for (Customer c : customers.values()) {
            System.out.println(c.toString());
        }
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}