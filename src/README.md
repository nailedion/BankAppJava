# Sistem Bancar - Gestiune Clienți și Conturi

## 1. Acțiuni / Interogări posibile (10)

1. Adăugarea unui client nou în sistem.

2. Deschiderea unui cont (Curent / Economii) pentru un client.

3. Căutarea unui client după ID și afișarea detaliilor.

4. Listarea tuturor clienților ordonați după ID.

5. Depunerea de fonduri într-un cont (cu validare).

6. Retragerea de fonduri din cont (cu verificarea soldului / overdraft).

7. Generarea extrasului de cont (listarea istoricului de tranzacții).

8. Actualizarea datelor unui client (modificare adresa de email).

9. Închiderea (ștergerea) unui cont bancar după IBAN.

10. Ștergerea unui client din sistem.

## 2. Tipuri de obiecte din domeniu (8+)

* Customer

* Account (clasa abstractă)

* SavingsAccount

* CheckingAccount

* Card

* Transaction (clasă imutabilă)

* Currency (Enum)

* TransactionType / TransactionStatus (Enums)