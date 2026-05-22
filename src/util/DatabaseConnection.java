package util;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private String url;

    private DatabaseConnection() {
        try {
            Properties props = new Properties();

            File file = new File("src/resources/db.properties");

            if (!file.exists()) {
                System.err.println("Nu gasesc db.properties la calea: " + file.getAbsolutePath());
                return;
            }

            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
                url = props.getProperty("db.url");
            }
            connect();
        } catch (Exception e) {
            System.err.println("Eroare la citirea db.properties: " + e.getMessage());
        }
    }

    private void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                System.err.println("Eroare driver SQLite: " + e.getMessage());
                return;
            }

            connection = DriverManager.getConnection(url);

            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            connect();
        } catch (SQLException e) {
            System.err.println("Eroare conexiune: " + e.getMessage());
        }
        return connection;
    }
}