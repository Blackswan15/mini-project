package com.legalsahayak.app.ui;

import com.legalsahayak.app.db.DatabaseService;
import java.util.Map;

public class UIHandler {

    public static void displayWelcomeMessage() {
        System.out.println("======================================");
        System.out.println("   Welcome to Legal Sahayak");
        System.out.println(" Your Console-Based Legal Assistant");
        System.out.println("======================================");
    }

    public static void displayAuthMenu() {
        System.out.println("\n--- Authentication Menu ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void displayMainMenu(DatabaseService dbService) {
        System.out.println("\n--- Main Menu ---");
        
        Map<Integer, String> menuItems = dbService.getMainMenu();
        
        for (Map.Entry<Integer, String> entry : menuItems.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }
        
        System.out.print("Enter your choice: ");
    }

    public static void displayExitMessage() {
        System.out.println("\nThank you for using Legal Sahayak. Stay informed.");
    }
}