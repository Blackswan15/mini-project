package com.legalsahayak.app.ui;

public class UIHandler {
    public static void displayWelcomeMessage() {
        System.out.println("======================================");
        System.out.println("   Welcome to Legal Sahayak");
        System.out.println(" Your Console-Based Legal Assistant");
        System.out.println("======================================");
    }

    public static void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Legal Rights Navigator");
        System.out.println("2. Add Entry to Evidence Log");
        System.out.println("3. View Evidence Log");
        System.out.println("4. 'Know Your Rights' Quiz");
        System.out.println("5. RTI Request Generator"); // <-- ADDED
        System.out.println("6. Exit"); // <-- UPDATED
        System.out.print("Enter your choice: ");
    }

    public static void displayExitMessage() {
        System.out.println("\nThank you for using Legal Sahayak. Stay informed.");
    }
}