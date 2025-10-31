package com.legalsahayak.app.service;

import com.legalsahayak.app.db.DatabaseService;
import com.legalsahayak.app.model.LawyerProfile;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles all Admin-only CRUD operations.
 */
public class AdminService {
    private final Scanner scanner;
    private final DatabaseService dbService;

    public AdminService(Scanner scanner, DatabaseService dbService) {
        this.scanner = scanner;
        this.dbService = dbService;
    }

    /**
     * Main menu for Admin.
     */
    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Manage Lawyers");
            System.out.println("2. Manage Legal Info (Summaries/Rights)");
            System.out.println("3. Manage Jargon");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": lawyerMenu(); break;
                case "2": legalInfoMenu(); break;
                case "3": jargonMenu(); break;
                case "4": running = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // --- Helper to Select Category ---
    private int selectCategory() {
        System.out.println("Select a legal category:");
        Map<Integer, String> cats = dbService.getLegalCategories();
        cats.forEach((id, name) -> System.out.println(id + ". " + name));
        System.out.print("Enter category ID: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }

    // --- 1. LAWYER MANAGEMENT ---
    private void lawyerMenu() {
        System.out.println("\n-- Manage Lawyers --");
        System.out.println("1. Add Lawyer");
        System.out.println("2. View Lawyers");
        System.out.println("3. Update Lawyer");
        System.out.println("4. Delete Lawyer");
        System.out.print("Enter choice: ");
        
        switch (scanner.nextLine()) {
            case "1": addLawyer(); break;
            case "2": viewLawyers(); break;
            case "3": updateLawyer(); break;
            case "4": deleteLawyer(); break;
        }
    }

    private void addLawyer() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Specialization: ");
        String spec = scanner.nextLine();
        System.out.print("Enter Experience: ");
        String exp = scanner.nextLine();
        System.out.print("Enter Phone: ");
        String ph = scanner.nextLine();
        System.out.print("Enter Email: ");
        String mail = scanner.nextLine();
        int catId = selectCategory();
        if (catId == -1) { System.out.println("Invalid category."); return; }
        
        if (dbService.addLawyer(name, spec, exp, ph, mail, catId)) {
            System.out.println("Lawyer added successfully.");
        } else {
            System.out.println("Failed to add lawyer.");
        }
    }

    private void viewLawyers() {
        int catId = selectCategory();
        if (catId == -1) { System.out.println("Invalid category."); return; }
        List<LawyerProfile> lawyers = dbService.getLawyers(catId);
        if (lawyers.isEmpty()) {
            System.out.println("No lawyers found for this category.");
            return;
        }
        System.out.println("\n--- Lawyers ---");
        lawyers.forEach(System.out::println);
    }
    
    private void updateLawyer() {
        System.out.print("Enter ID of lawyer to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter field (phone, email, specialization): ");
        String field = scanner.nextLine().toLowerCase();
        
        if (!field.equals("phone") && !field.equals("email") && 
            !field.equals("specialization")) {
            System.out.println("Invalid field.");
            return;
        }
        System.out.print("Enter new value: ");
        String value = scanner.nextLine();
        
        if (dbService.updateLawyer(id, field, value)) {
            System.out.println("Lawyer updated.");
        } else {
            System.out.println("Update failed. Check ID.");
        }
    }

    private void deleteLawyer() {
        System.out.print("Enter ID of lawyer to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        if (dbService.deleteLawyer(id)) {
            System.out.println("Lawyer deleted.");
        } else {
            System.out.println("Delete failed. Check ID.");
        }
    }
    // --- 2. LEGAL INFO MANAGEMENT ---
    private void legalInfoMenu() {
        System.out.println("\n-- Manage Legal Info --");
        System.out.println("Note: This is for adding/updating summaries " +
                           "and key rights.");
        
        int catId = selectCategory();
        if (catId == -1) { System.out.println("Invalid category."); return; }
        
        System.out.print("Enter info type ('summary' or 'key_rights'): ");
        String infoType = scanner.nextLine().toLowerCase();
        
        if (!infoType.equals("summary") && !infoType.equals("key_rights")) {
            System.out.println("Invalid info type.");
            return;
        }
        
        System.out.println("Current content: " + 
                           dbService.getLegalInfo(catId, infoType));
        System.out.println("Enter new content (long text, one line):");
        String content = scanner.nextLine();
        
        if (dbService.updateLegalInfo(catId, infoType, content)) {
            System.out.println("Legal info updated successfully.");
        } else {
            System.out.println("Failed to update info.");
        }
    }

    // --- 3. JARGON MANAGEMENT ---
    private void jargonMenu() {
        System.out.println("\n-- Manage Jargon --");
        System.out.println("1. Add/Update Jargon Term");
        System.out.println("2. Delete Jargon Term");
        System.out.print("Enter choice: ");
        
        switch (scanner.nextLine()) {
            case "1": addJargon(); break;
            case "2": deleteJargon(); break;
        }
    }

    private void addJargon() {
        System.out.print("Enter term (e.g., 'POCSO', 'FIR'): ");
        String term = scanner.nextLine().toUpperCase();
        System.out.print("Enter definition: ");
        String definition = scanner.nextLine();
        
        if (dbService.addJargon(term, definition)) {
            System.out.println("Jargon term saved.");
        } else {
            System.out.println("Failed to save term.");
        }
    }

    private void deleteJargon() {
        System.out.print("Enter term to delete: ");
        String term = scanner.nextLine().toUpperCase();
        
        if (dbService.deleteJargon(term)) {
            System.out.println("Jargon term deleted.");
        } else {
            System.out.println("Failed to delete term. (Not found?)");
        }
    }
}