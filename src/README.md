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


## Etapa II

### 1. Bază de date relațională + JDBC

#### 1.1 — Schema bazei de date
- **Fișier `schema.sql`**: Inclus în `src/schema.sql`. Conține `CREATE TABLE` pentru cele 6 entități salvate (`customers`, `accounts`, `savings_accounts`, `checking_accounts`, `transactions`, `cards`), chei primare, multiple relații de tip `FOREIGN KEY` (ex: `customer_id` din `accounts`, `account_iban` din restul tabelelor) și comenzi `DROP TABLE IF EXISTS` la început pentru re-rulare curată.
- **Fișier `db.properties`**: Găsit în `src/resources/db.properties`, conține URL-ul bazei de date SQLite (`db.url=jdbc:sqlite:banca.db`), nefiind hardcodat direct în codul Java.

#### 1.2 — Conexiunea la baza de date
- **Clasa `DatabaseConnection`**: Implementată în `src/util/DatabaseConnection.java` respectând șablonul **Singleton**. Citește configurația din `db.properties` prin `FileInputStream` și expune un obiect `Connection` reutilizabil.

#### 1.3 — Interfață generică Repository
- **Interfața generică**: Definită corect în `src/repository/Repository.java` cu metodele: `save`, `findById`, `findAll`, `update`, `delete`.
- **Implementări concrete (4)**: Există clase concrete care implementează această interfață pentru entitățile principale:
    1. `AccountRepository`
    2. `CustomerRepository`
    3. `CardRepository`
    4. `TransactionRepository`
- **`PreparedStatement` & `try-with-resources`**: Toate repository-urile (ex. `CustomerRepository.java`) folosesc sintaxa `try (Connection conn = ...; PreparedStatement pstmt = ...)` pentru a închide automat resursele și folosesc `PreparedStatement` pentru a preveni SQL injection.

### 2. Tranzacții JDBC
- **Tranzacție explicită implementată**: În clasa `AccountRepository.java`, metoda `saveWithTransaction(Account account, int customerId)` implementează tranzacția explicită.
    - Setează `conn.setAutoCommit(false)`.
    - Execută două inserări concomitente care afectează tabele diferite: întâi inserează datele de bază în `accounts`, apoi apelează al doilea tabel (`savings_accounts` sau `checking_accounts`) în funcție de instanța contului.
    - Se finalizează printr-un bloc `commit()` pentru succes sau `rollback()` în caz de `SQLException` (plus readucerea la `setAutoCommit(true)`).

### 3. Interogări avansate cu JOIN
- **Metoda 1**: `AccountRepository.findById(String iban)` folosește `LEFT JOIN` între `accounts`, `savings_accounts` și `checking_accounts` pentru a asambla obiectul corect la rulare.
- **Metoda 2**: `AccountRepository.findAllByCustomerId(int customerId)` utilizează aceeași combinație complexă de `LEFT JOIN`-uri pentru a prelua toată schema completă de conturi a unui utilizator pe baza ID-ului său.
- **Metoda 3**: `AccountService.showAdvancedJoinReport()` generează un raport complet realizând un `INNER JOIN` între `customers` (pentru nume) și `accounts` (pentru sold și IBAN), formatând rezultatul direct în consolă.

### 4. Serviciu de audit
- **Clasa `AuditService`**: Implementată în `src/service/AuditService.java` respectând șablonul Singleton.
- **Scriere CSV**: Salvează automat în fișierul `audit.csv` folosind formatul indicat: `nume_actiune,timestamp` (ex: `2026-05-22T16:39:32`). Modul *append* este activat (`new FileWriter(FILE_PATH, true)`) pentru a păstra tot istoricul.
- **Thread-safe**: Clasa folosește utilitarul modern `ReentrantLock` (`lock.lock()` și `lock.unlock()` în bloc *finally*) pentru sincronizarea scrierilor concurente.
- **Acoperire integrală**: Serviciul de audit este apelat direct la primele rânduri din metodele `CustomerService` și `AccountService` (precum `addCustomer`, `deposit`, `withdraw`, `deleteCustomer`, etc.) pentru toate cele 10 acțiuni definite în Etapa 1.