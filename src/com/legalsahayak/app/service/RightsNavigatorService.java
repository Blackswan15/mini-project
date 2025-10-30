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
        System.out.println("\n--- ⚖️ Legal Rights Navigator ---");
        System.out.println("Select a category to learn more:");
        
        Map<Integer, String> categories = dbService.getLegalCategories();
        if (categories.isEmpty()) {
            System.out.println("No legal categories found.");
            return;
        }
        
        categories.forEach((id, name) -> System.out.println(id + ". " + name));
        System.out.print("Your choice: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            String categoryName = categories.get(choice);
            
            if (categoryName == null) {
                System.out.println("Invalid category.");
                return;
            }
            
            String definition = dbService.getLegalInfo(choice, "DEFINITION");
            System.out.println("\n--- Definition ---\n" + definition);

            System.out.print("\nSee relevant laws? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                String laws = dbService.getLegalInfo(choice, "LAWS");
                System.out.println("\n--- Relevant Laws ---\n" + laws);

                System.out.print("\nFind a lawyer? (y/n): ");
                if (scanner.nextLine().equalsIgnoreCase("y")) {
                    displayLawyerProfile(choice, categoryName);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private void displayLawyerProfile(int categoryId, String categoryName) {
        System.out.println("\n--- Lawyers for: " + categoryName + " ---");
        List<LawyerProfile> lawyers = dbService.getLawyers(categoryId);
        
        if (lawyers.isEmpty()) {
            System.out.println("No specialized lawyers found.");
        } else {
            for (LawyerProfile lawyer : lawyers) {
                System.out.println("----------------------------------------");
                System.out.println(lawyer.toString());
            }
        }
        System.out.println("----------------------------------------");
    }
}