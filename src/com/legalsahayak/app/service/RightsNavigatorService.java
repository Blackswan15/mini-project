package com.legalsahayak.app.service;

import com.legalsahayak.app.db.DatabaseService;
import com.legalsahayak.app.model.LawyerProfile;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RightsNavigatorService {
    private final Scanner scanner;
    private final DatabaseService dbService;

    public RightsNavigatorService(Scanner scanner, DatabaseService dbService) {
        this.scanner = scanner;
        this.dbService = dbService;
    }

    public void start() {
        System.out.println("\n--- Rights Navigator ---");
        System.out.println("Select a legal category to learn about:");
        
        Map<Integer, String> categories = dbService.getLegalCategories();
        categories.forEach((id, name) -> System.out.println(id + ". " + name));
        
        System.out.print("Enter category ID: ");
        int catId = -1;
        try {
            catId = Integer.parseInt(scanner.nextLine());
            if (!categories.containsKey(catId)) {
                System.out.println("Invalid category ID.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println("\nSelect information for " + 
                               categories.get(catId) + ":");
            System.out.println("1. Summary");
            System.out.println("2. Key Rights");
            System.out.println("3. Find Lawyers");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            switch (scanner.nextLine()) {
                case "1": 
                    System.out.println(dbService.getLegalInfo(catId, "summary"));
                    break;
                case "2":
                    System.out.println(dbService.getLegalInfo(catId, "key_rights"));
                    break;
                case "3":
                    displayLawyers(catId);
                    break;
                case "4":
                    running = false;
                    break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void displayLawyers(int catId) {
        List<LawyerProfile> lawyers = dbService.getLawyers(catId);
        if (lawyers.isEmpty()) {
            System.out.println("No lawyers found for this category.");
            return;
        }
        System.out.println("\n--- Lawyers ---");
        lawyers.forEach(System.out::println);
    }
}