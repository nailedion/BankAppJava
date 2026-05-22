package service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

public class AuditService {
    private static final String FILE_PATH = "audit.csv";
    private final ReentrantLock lock = new ReentrantLock();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private AuditService() {}
    private static class Holder { private static final AuditService INSTANCE = new AuditService(); }
    public static AuditService getInstance() { return Holder.INSTANCE; }

    public void logAction(String actionName) {
        lock.lock();
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.println(actionName + "," + LocalDateTime.now().format(formatter));
        } catch (IOException e) {
            System.out.println("Eroare la scrierea in fisierul de audit: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}