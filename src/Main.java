import module.CheckingAccount;
import module.Currency;
import module.SavingsAccount;
import service.BankService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BankService bankService = BankService.getInstance();

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        System.out.println("=========================================");
        System.out.println("   Sistem Bancar - Gestiune Clienti      ");
        System.out.println("=========================================");

        //
        bankService.addCustomer(1, "Andrei", "Ionescu", "andrei@email.com");
        bankService.addAccountToCustomer(1, new SavingsAccount("RO01BANC123", 1000, Currency.RON, 4.5));
        //

        while (isRunning) {
            System.out.println("\n--- MENIU PRINCIPAL ---");
            System.out.println("1.  Adauga un client nou");
            System.out.println("2.  Adauga un cont nou unui client (Economii/Curent)");
            System.out.println("3.  Afiseaza toti clientii (Sortati dupa ID)");
            System.out.println("4.  Afiseaza conturile unui client");
            System.out.println("5.  Cauta informatii cont dupa IBAN");
            System.out.println("6.  Depune bani in cont");
            System.out.println("7.  Retrage bani din cont");
            System.out.println("8.  Afiseaza istoricul de tranzactii (Extras de cont)");
            System.out.println("9.  Modifica adresa de email a unui client");
            System.out.println("10. Iesire");
            System.out.print("Selectati optiunea: ");

            int option = -1;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Eroare: Va rugam sa introduceti un numar valid.");
                continue;
            }

            switch (option) {
                case 1:
                    System.out.print("ID Client: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Prenume: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Nume: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    bankService.addCustomer(id, firstName, lastName, email);
                    break;

                case 2:
                    System.out.print("ID Client pentru care se deschide contul: ");
                    int cid = Integer.parseInt(scanner.nextLine());
                    System.out.print("IBAN: ");
                    String iban = scanner.nextLine();
                    System.out.println("Tip cont: 1 - Economii (cu dobanda) | 2 - Curent (cu overdraft)");
                    int tip = Integer.parseInt(scanner.nextLine());

                    if (tip == 1) {
                        bankService.addAccountToCustomer(cid, new SavingsAccount(iban, 0, Currency.RON, 3.0));
                    } else if (tip == 2) {
                        bankService.addAccountToCustomer(cid, new CheckingAccount(iban, 0, Currency.RON, 500));
                    } else {
                        System.out.println("Tip invalid!");
                    }
                    break;

                case 3:
                    bankService.showAllCustomers();
                    break;

                case 4:
                    System.out.print("ID Client: ");
                    int searchId = Integer.parseInt(scanner.nextLine());
                    bankService.showCustomerAccounts(searchId);
                    break;

                case 5:
                    System.out.print("IBAN de cautat: ");
                    String findIban = scanner.nextLine();
                    try {
                        System.out.println(bankService.findAccountByIban(findIban));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 6:
                    System.out.print("IBAN: ");
                    String dIban = scanner.nextLine();
                    System.out.print("Suma de depus: ");
                    double dSum = Double.parseDouble(scanner.nextLine());
                    bankService.depositToAccount(dIban, dSum);
                    break;

                case 7:
                    System.out.print("IBAN: ");
                    String wIban = scanner.nextLine();
                    System.out.print("Suma de retras: ");
                    double wSum = Double.parseDouble(scanner.nextLine());
                    bankService.withdrawFromAccount(wIban, wSum);
                    break;

                case 8:
                    System.out.print("IBAN: ");
                    String tIban = scanner.nextLine();
                    bankService.showAccountTransactions(tIban);
                    break;

                case 9:
                    System.out.print("ID Client: ");
                    int uId = Integer.parseInt(scanner.nextLine());
                    var client = bankService.findCustomerById(uId);
                    if (client != null) {
                        System.out.print("Email nou: ");
                        client.setEmail(scanner.nextLine());
                        System.out.println("Actualizare reusita!");
                    } else {
                        System.out.println("Clientul nu a fost gasit.");
                    }
                    break;

                case 10:
                    isRunning = false;
                    System.out.println("Program incheiat. O zi buna!");
                    break;

                default:
                    System.out.println("Optiunea nu exista in meniu!");
            }
        }
        scanner.close();
    }
}