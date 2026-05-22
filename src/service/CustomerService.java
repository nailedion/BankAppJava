package service;

import exception.CustomerNotFoundException;
import model.Customer;
import repository.CustomerRepository;
import repository.AccountRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CustomerService {
    private final CustomerRepository customerRepository = CustomerRepository.getInstance();

    private CustomerService() {}
    private static class Holder { private static final CustomerService INSTANCE = new CustomerService(); }
    public static CustomerService getInstance() { return Holder.INSTANCE; }

    public void updateCustomer(Customer customer) {
        AuditService.getInstance().logAction("updateCustomer");
        customerRepository.update(customer);
    }

    public void addCustomer(String firstName, String lastName, String email) {
        AuditService.getInstance().logAction("addCustomer");
        Customer newCustomer = new Customer(firstName, lastName, email);
        Customer saved = customerRepository.save(newCustomer);
        System.out.println("    Client adaugat automat cu ID [" + saved.getId() + "]: " + saved.getFirstName() + " " + saved.getLastName());
    }

    public Customer findCustomerById(Integer id) throws CustomerNotFoundException {
        AuditService.getInstance().logAction("findCustomerById");
        Optional<Customer> c = customerRepository.findById(id);
        if (c.isEmpty()) {
            throw new CustomerNotFoundException("Clientul cu ID " + id + " nu a fost gasit in baza de date!");
        }

        Customer customer = c.get();
        AccountRepository.getInstance().findAllByCustomerId(customer.getId()).forEach(customer::addAccount);
        return customer;
    }

    public void deleteCustomer(Integer id) {
        AuditService.getInstance().logAction("deleteCustomer");
        try {
            findCustomerById(id);
            customerRepository.delete(id);
            System.out.println("    Clientul cu ID " + id + " a fost sters definitiv din baza de date.");
        } catch (CustomerNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    public void showAllCustomers() {
        AuditService.getInstance().logAction("showAllCustomers");
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            System.out.println("Nu exista clienti in sistem.");
            return;
        }
        System.out.println("Lista Clienti din Baza de Date (Ordonati dupa ID):");
        for (Customer c : customers) {
            AccountRepository.getInstance().findAllByCustomerId(c.getId()).forEach(c::addAccount);
            System.out.println(c.toString());
        }
    }

    public Collection<Customer> getAllCustomers() {
        AuditService.getInstance().logAction("getAllCustomers");
        List<Customer> list = customerRepository.findAll();
        list.forEach(c -> AccountRepository.getInstance().findAllByCustomerId(c.getId()).forEach(c::addAccount));
        return list;
    }
}