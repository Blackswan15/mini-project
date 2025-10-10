package com.legalsahayak.app.service;

import java.util.ArrayList;
import java.util.Scanner;
import com.legalsahayak.app.model.LogEntry;

public class EvidenceLogService {
    private final ArrayList<LogEntry> entries = new ArrayList<>();
    private final Scanner scanner;

    public EvidenceLogService(Scanner scanner) {
        this.scanner = scanner;
    }

    public void addEntry() {
        System.out.println("\n--- Add New Log Entry ---");
        String category = getCategoryFromUser();
        System.out.print("Describe the incident/activity: ");
        String description = scanner.nextLine();
        entries.add(new LogEntry(description, category));
        System.out.println("✅ Entry saved successfully under '" + category + "'.");
    }

    public void viewEntries() {
        System.out.println("\n--- View Log Entries ---");
        if (entries.isEmpty()) {
            System.out.println("Your log is currently empty.");
            return;
        }

        System.out.println("1. View All Entries");
        System.out.println("2. View by Category");
        System.out.print("Your choice: ");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            printEntries(entries);
        } else if (choice.equals("2")) {
            String category = getCategoryFromUser();
            ArrayList<LogEntry> filteredEntries = new ArrayList<>();
            for (LogEntry entry : entries) {
                if (entry.getCategory().equals(category)) {
                    filteredEntries.add(entry);
                }
            }
            printEntries(filteredEntries);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void printEntries(ArrayList<LogEntry> entryList) {
        if (entryList.isEmpty()) {
            System.out.println("\nNo entries found for this category.");
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