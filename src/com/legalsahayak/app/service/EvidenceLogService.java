package com.legalsahayak.app.service;

import com.legalsahayak.app.db.DatabaseService;
import com.legalsahayak.app.model.LogEntry;
import java.util.List;
import java.util.Scanner;

public class EvidenceLogService {
    
    private final Scanner scanner;
    private final DatabaseService dbService;

    public EvidenceLogService(Scanner scanner, DatabaseService dbService) {
        this.scanner = scanner;
        this.dbService = dbService;
    }

    public void addEntry(String username) {
        System.out.println("\n--- Add New Log Entry ---");
        String category = getCategoryFromUser();
        System.out.print("Describe the incident/activity: ");
        String description = scanner.nextLine();
        
        if (description.isEmpty()) {
            System.out.println("Description cannot be empty.");
            return;
        }

        dbService.addLogEntry(username, description, category);
        System.out.println("✅ Entry saved successfully.");
    }

    public void viewEntries(String username) {
        System.out.println("\n--- View Log Entries ---");
        System.out.println("1. View All Entries");
        System.out.println("2. View by Category");
        System.out.print("Your choice: ");
        String choice = scanner.nextLine();
        
        List<LogEntry> entries;
        if (choice.equals("1")) {
            entries = dbService.getLogEntries(username, null); // null = all
        } else if (choice.equals("2")) {
            String category = getCategoryFromUser();
            entries = dbService.getLogEntries(username, category);
        } else {
            System.out.println("Invalid choice.");
            return;
        }
        
        printEntries(entries);
    }

    private void printEntries(List<LogEntry> entryList) {
        if (entryList.isEmpty()) {
            System.out.println("\nNo entries found.");
            return;
        }
        System.out.println();
        for (LogEntry entry : entryList) {
            System.out.println(entry.toString());
        }
        System.out.println("----------------------------------------");
    }

    private String getCategoryFromUser() {
        while (true) {
            System.out.println("Select a category:");
            System.out.println("1. Daily Activities");
            System.out.println("2. Problems Faced");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine();
            if ("1".equals(choice)) return "Daily Activities";
            if ("2".equals(choice)) return "Problems Faced";
            System.out.println("Invalid choice. Please enter 1 or 2.");
        }
    }
}