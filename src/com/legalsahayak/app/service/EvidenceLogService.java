package com.legalsahayak.app.service;

import com.legalsahayak.app.db.DatabaseService;
import com.legalsahayak.app.model.LogEntry;
import java.util.List; // Using List data structure
import java.util.Scanner;

public class EvidenceLogService {
    private final Scanner scanner;
    private final DatabaseService dbService;

    public EvidenceLogService(Scanner scanner, DatabaseService dbService) {
        this.scanner = scanner;
        this.dbService = dbService;
    }

    /**
     * Main menu for the Evidence Log (User CRUD).
     */
    public void start(String username) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Evidence Log Menu ---");
            System.out.println("1. Add New Log Entry (Create)");
            System.out.println("2. View Log Entries (Read)");
            System.out.println("3. Update Log Entry (Update)");
            System.out.println("4. Delete Log Entry (Delete)");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            switch (scanner.nextLine()) {
                case "1": addEntry(username); break;
                case "2": viewEntries(username); break;
                case "3": updateEntry(username); break;
                case "4": deleteEntry(username); break;
                case "5": running = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // CREATE
    private void addEntry(String username) {
        System.out.print("Enter category (e.g., Harassment, Incident): ");
        String category = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        
        dbService.addLogEntry(username, description, category);
        System.out.println("Log entry saved.");
    }

    // READ
    private void viewEntries(String username) {
        System.out.print("Filter by category (or press Enter to view all): ");
        String category = scanner.nextLine();
        if (category.isBlank()) {
            category = null;
        }

        List<LogEntry> entries = dbService.getLogEntries(username, category);
        if (entries.isEmpty()) {
            System.out.println("No log entries found.");
            return;
        }
        
        System.out.println("\n--- Your Log Entries ---");
        for (LogEntry entry : entries) {
            System.out.println(entry);
        }
    }

    // UPDATE
    private void updateEntry(String username) {
        System.out.print("Enter the ID of the log entry to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter the new description: ");
        String newDesc = scanner.nextLine();
        
        if (dbService.updateLogEntry(id, newDesc, username)) {
            System.out.println("Log entry updated successfully.");
        } else {
            System.out.println("Error: Could not find log entry with that ID.");
        }
    }

    // DELETE
    private void deleteEntry(String username) {
        System.out.print("Enter the ID of the log entry to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Are you sure? (y/n): ");
        
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            if (dbService.deleteLogEntry(id, username)) {
                System.out.println("Log entry deleted successfully.");
            } else {
                System.out.println("Error: Could not find log entry.");
            }
        }
    }
}