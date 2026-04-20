import exception.CustomerNotFoundException;
import model.CheckingAccount;
import model.Currency;
import model.SavingsAccount;
import service.AccountService;
import service.CustomerService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        CustomerService customerService = CustomerService.getInstance();
//        AccountService accountService = AccountService.getInstance();
//
//        System.out.println("1. Adaugare clienti");
//        customerService.addCustomer(1, "Andrei", "Ionescu", "andrei@email.com");
//        customerService.addCustomer(2, "Maria", "Popescu", "maria@email.com");
//
//        System.out.println("\n2. Deschidere conturi");
//        accountService.addAccountToCustomer(1, new SavingsAccount("RO01BANC111", 0, Currency.RON, 4.5));
//        accountService.addAccountToCustomer(2, new CheckingAccount("RO02BANC222", 0, Currency.EUR, 500));
//
//        System.out.println("\n3. Cautare client (ID 1)");
//        try {
//            System.out.println(customerService.findCustomerById(1));
//        } catch (CustomerNotFoundException e) {
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("\n4. Listare clienti");
//        customerService.showAllCustomers();
//
//        System.out.println("\n5. Depunere numerar (RO01BANC111)");
//        accountService.deposit("RO01BANC111", 2000);
//
//        System.out.println("\n6. Retragere numerar (RO01BANC111)");
//        accountService.withdraw("RO01BANC111", 500);
//
//        System.out.println("\n7. Afisare extras de cont");
//        accountService.showTransactions("RO01BANC111");
//
//        System.out.println("\n8. Actualizare email client ID 2");
//        try {
//            customerService.findCustomerById(2).setEmail("maria.nou@email.com");
//            System.out.println("Email actualizat cu succes: " + customerService.findCustomerById(2).getEmail());
//        } catch (CustomerNotFoundException e) {
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("\n9. Stergere cont (RO02BANC222)");
//        accountService.closeAccount("RO02BANC222");
//
//        System.out.println("\n10. Stergere client (ID 2)");
//        customerService.deleteCustomer(2);
//
//        System.out.println("\nLista Finala dupa stergere");
//        customerService.showAllCustomers();



        CustomerService customerService = CustomerService.getInstance();
        AccountService accountService = AccountService.getInstance();
        Scanner scanner = new Scanner(System.in);

        customerService.addCustomer(1, "Andrei", "Ionescu", "andrei@email.com");
        customerService.addCustomer(2, "Maria", "Popescu", "maria@email.com");
        accountService.addAccountToCustomer(1, new SavingsAccount("RO01BANC111", 0, Currency.RON, 4.5));
        accountService.addAccountToCustomer(2, new CheckingAccount("RO02BANC222", 0, Currency.EUR, 500));

        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n=== MENIU PRINCIPAL BANCA ===");
            System.out.println("1. Adaugare client nou");
            System.out.println("2. Deschidere cont nou pentru un client");
            System.out.println("3. Cautare client (dupa ID)");
            System.out.println("4. Listare toti clientii");
            System.out.println("5. Depunere numerar intr-un cont");
            System.out.println("6. Retragere numerar dintr-un cont");
            System.out.println("7. Afisare extras de cont (istoric tranzactii)");
            System.out.println("8. Actualizare adresa de email client");
            System.out.println("9. Inchidere/Stergere cont bancar");
            System.out.println("10. Stergere client din sistem");
            System.out.println("0. IESIRE");
            System.out.print("Alege o optiune: ");

            int option = -1;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Eroare: Te rog introdu un numar valid!");
                continue;
            }

            switch (option) {
                case 1:
                    System.out.print("ID Client nou: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Prenume: ");
                    String fName = scanner.nextLine();
                    System.out.print("Nume: ");
                    String lName = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    customerService.addCustomer(id, fName, lName, email);
                    break;

                case 2:
                    System.out.print("ID Client existent: ");
                    int cId = Integer.parseInt(scanner.nextLine());
                    System.out.print("IBAN nou: ");
                    String iban = scanner.nextLine();
                    System.out.print("Tip cont (1 - Economii, 2 - Curent): ");
                    int type = Integer.parseInt(scanner.nextLine());

                    if (type == 1) {
                        System.out.print("Rata dobanda (%): ");
                        double interest = Double.parseDouble(scanner.nextLine());
                        accountService.addAccountToCustomer(cId, new SavingsAccount(iban, 0, Currency.RON, interest));
                    } else if (type == 2) {
                        System.out.print("Limita overdraft (RON): ");
                        double overdraft = Double.parseDouble(scanner.nextLine());
                        accountService.addAccountToCustomer(cId, new CheckingAccount(iban, 0, Currency.RON, overdraft));
                    } else {
                        System.out.println("Optiune tip cont invalida!");
                    }
                    break;

                case 3:
                    System.out.print("Introdu ID Client pentru cautare: ");
                    int searchId = Integer.parseInt(scanner.nextLine());
                    try {
                        System.out.println(customerService.findCustomerById(searchId));
                    } catch (CustomerNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    customerService.showAllCustomers();
                    break;

                case 5:
                    System.out.print("Introdu IBAN pentru depunere: ");
                    String depIban = scanner.nextLine();
                    System.out.print("Suma de depus: ");
                    double depSum = Double.parseDouble(scanner.nextLine());
                    accountService.deposit(depIban, depSum);
                    break;

                case 6:
                    System.out.print("Introdu IBAN pentru retragere: ");
                    String withIban = scanner.nextLine();
                    System.out.print("Suma de retras: ");
                    double withSum = Double.parseDouble(scanner.nextLine());
                    accountService.withdraw(withIban, withSum);
                    break;

                case 7:
                    System.out.print("Introdu IBAN pentru extrasul de cont: ");
                    String statementIban = scanner.nextLine();
                    accountService.showTransactions(statementIban);
                    break;

                case 8:
                    System.out.print("ID Client pentru actualizare email: ");
                    int updateId = Integer.parseInt(scanner.nextLine());
                    try {
                        var clientToUpdate = customerService.findCustomerById(updateId);
                        System.out.print("Adresa email noua: ");
                        String newEmail = scanner.nextLine();
                        clientToUpdate.setEmail(newEmail);
                        System.out.println("Adresa de email a fost actualizata cu succes!");
                    } catch (CustomerNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 9:
                    System.out.print("Introdu IBAN-ul contului pe care vrei sa il inchizi: ");
                    String closeIban = scanner.nextLine();
                    accountService.closeAccount(closeIban);
                    break;

                case 10:
                    System.out.print("Introdu ID-ul clientului pe care vrei sa il stergi: ");
                    int delId = Integer.parseInt(scanner.nextLine());
                    customerService.deleteCustomer(delId);
                    break;

                case 0:
                    isRunning = false;
                    System.out.println("Iesire din sistem. La revedere!");
                    break;

                default:
                    System.out.println("Optiune invalida! Te rog alege un numar intre 0 si 10.");
            }
        }
        scanner.close();
    }
}