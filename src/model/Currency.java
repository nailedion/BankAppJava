package model;

public enum Currency {
    RON("Leu Românesc", "RON"),
    EUR("Euro", "€"),
    CHF("Franc Suisse", "CHF");

    private final String fullName;
    private final String symbol;

    Currency(String fullName, String symbol) {
        this.fullName = fullName;
        this.symbol = symbol;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSymbol() {
        return symbol;
    }
}