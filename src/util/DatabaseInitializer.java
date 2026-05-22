package util;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();

            if (conn == null) {
                System.err.println("Conexiunea este null.");
                return;
            }

            ResultSet rs = conn.getMetaData().getTables(null, null, "customers", null);
            if (rs.next()) {
                System.out.println("Baza de date a fost incarcata.");
                return;
            }

            File file = new File("src/schema.sql");

            if (!file.exists()) {
                System.err.println("Nu gasesc schema.sql la calea: " + file.getAbsolutePath());
                return;
            }

            String schemaSql = Files.readString(file.toPath());

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(schemaSql);
                System.out.println("Baza de date a fost initializata de la zero cu succes!");
            }

        } catch (Exception e) {
            System.err.println("Eroare: " + e.getMessage());
        }
    }
}