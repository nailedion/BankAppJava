import exception.CustomerNotFoundException;
import model.CheckingAccount;
import model.Currency;
import model.SavingsAccount;
import service.AccountService;
import service.CustomerService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        util.DatabaseInitializer.initialize();

        CustomerService customerService = CustomerService.getInstance();
        AccountService accountService = AccountService.getInstance();
        Scanner scanner = new Scanner(System.in);

        boolean isRunning = true;

        while (isRunning) {
            System.out.println("1. Adaugare client nou");
            System.out.println("2. Deschidere cont nou pentru un client");
            System.out.println("3. Cautare client dupa ID");
            System.out.println("4. Listare toti clientii din DB");
            System.out.println("5. Depunere numerar intr-un cont");
            System.out.println("6. Retragere numerar dintr-un cont");
            System.out.println("7. Afisare extras de cont");
            System.out.println("8. Actualizare adresa de email client");
            System.out.println("9. Inchidere/Stergere cont bancar");
            System.out.println("10. Stergere client din sistem");
            System.out.println("11. Afisare Raport Global Clienti + Conturi");
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
                    System.out.print("Prenume: ");
                    String fName = scanner.nextLine();
                    System.out.print("Nume: ");
                    String lName = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    customerService.addCustomer(fName, lName, email);
                    break;

                case 2:
                    System.out.print("ID Client existent: ");
                    int cId = Integer.parseInt(scanner.nextLine());
                    System.out.print("IBAN nou: ");
                    String iban = scanner.nextLine();
                    System.out.print("Tip cont (1 - Economii, 2 - Curent): ");
                    int type = Integer.parseInt(scanner.nextLine());

                    if (type == 1) {
                        System.out.print("Rata dobanda(%): ");
                        double interest = Double.parseDouble(scanner.nextLine());
                        accountService.addAccountToCustomer(cId, new SavingsAccount(iban, 0, Currency.RON, interest));
                    } else if (type == 2) {
                        System.out.print("Limita overdraft: ");
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

                        customerService.updateCustomer(clientToUpdate);

                        System.out.println("Adresa de email a fost actualizata cu succes in DB!");
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

                case 11:
                    accountService.showAdvancedJoinReport();
                    break;

                case 0:
                    isRunning = false;
                    System.out.println("Iesire din sistem. La revedere!");
                    break;

                default:
                    System.out.println("Optiune invalida! Te rog alege un numar intre 0 si 11.");
            }
        }
        scanner.close();
    }
}