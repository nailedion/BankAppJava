DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS cards;
DROP TABLE IF EXISTS savings_accounts;
DROP TABLE IF EXISTS checking_accounts;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
    id INTEGER PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL
);

CREATE TABLE accounts (
    iban TEXT PRIMARY KEY,
    customer_id INTEGER,
    balance REAL DEFAULT 0.0,
    currency TEXT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE TABLE savings_accounts (
    account_iban TEXT PRIMARY KEY,
    interest_rate REAL NOT NULL,
    FOREIGN KEY (account_iban) REFERENCES accounts(iban) ON DELETE CASCADE
);

CREATE TABLE checking_accounts (
    account_iban TEXT PRIMARY KEY,
    overdraft_limit REAL NOT NULL,
    FOREIGN KEY (account_iban) REFERENCES accounts(iban) ON DELETE CASCADE
);

CREATE TABLE transactions (
    id TEXT PRIMARY KEY,
    account_iban TEXT,
    amount REAL NOT NULL,
    currency TEXT NOT NULL,
    description TEXT,
    status TEXT NOT NULL,
    type TEXT NOT NULL,
    timestamp TEXT NOT NULL,
    FOREIGN KEY (account_iban) REFERENCES accounts(iban) ON DELETE CASCADE
);

CREATE TABLE cards (
    card_number TEXT PRIMARY KEY,
    account_iban TEXT,
    is_contactless INTEGER,
    expiry_date TEXT,
    FOREIGN KEY (account_iban) REFERENCES accounts(iban) ON DELETE CASCADE
);