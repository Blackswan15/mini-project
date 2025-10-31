package com.legalsahayak.app.service;

import com.legalsahayak.app.db.DatabaseService;
import java.util.Scanner;

public class RtiService {
    private final Scanner scanner;
    private final DatabaseService dbService;

    public RtiService(Scanner scanner, DatabaseService dbService) {
        this.scanner = scanner;
        this.dbService = dbService;
    }

   
    public void start(String username) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- RTI Service Menu ---");
            System.out.println("1. Generate New RTI Request (Create)");
            System.out.println("2. View My RTI Requests (Read)");
            System.out.println("3. Update RTI Status (Update)");
            System.out.println("4. Delete an RTI Request (Delete)");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            switch (scanner.nextLine()) {
                case "1": generateRTI(username); break;
                case "2": dbService.displayUserRTIRequests(username); break;
                case "3": updateStatus(username); break;
                case "4": deleteRequest(username); break;
                case "5": running = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // CREATE
    private void generateRTI(String username) {
        System.out.println("--- New RTI Application ---");
        System.out.print("Enter your Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your Full Address: ");
        String addr = scanner.nextLine();
        System.out.print("Enter Public Authority (e.g., 'Municipal Corp'): ");
        String auth = scanner.nextLine();
        System.out.print("Enter Information Needed: ");
        String info = scanner.nextLine();

        // Simple text generation
        String rtiText = String.format(
            "To: The Public Information Officer\n%s\n\n" +
            "Subject: Request for Information under RTI Act 2005\n\n" +
            "Applicant: %s\nAddress: %s\n\n" +
            "Details of Information Required:\n%s\n\n" +
            "Please provide this information as per the RTI Act.",
            auth, name, addr, info
        );
        
        System.out.println("\n--- Generated RTI Text ---");
        System.out.println(rtiText);
        System.out.println("--------------------------");
        System.out.print("Save this request to your records? (y/n): ");

        if (scanner.nextLine().equalsIgnoreCase("y")) {
            dbService.saveRTIRequest(username, auth, info, name, 
                                     addr, rtiText);
            System.out.println("RTI request saved.");
        }
    }

    // UPDATE
    private void updateStatus(String username) {
        System.out.print("Enter the ID of the RTI request to update: ");
        if (!dbService.displayUserRTIRequests(username)) {
            return; // No requests to update
        }

        try {
            int rtiId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter new status (e.g., 'Filed', 'Replied'): ");
            String status = scanner.nextLine();
            dbService.updateRTIStatus(rtiId, status, username);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
        }
    }

    // DELETE
    private void deleteRequest(String username) {
        System.out.print("Enter the ID of the RTI request to delete: ");
        if (!dbService.displayUserRTIRequests(username)) {
            return; // No requests to delete
        }
        
        try {
            int rtiId = Integer.parseInt(scanner.nextLine());
            System.out.print("Are you sure? This cannot be undone. (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                if (dbService.deleteRTIRequest(rtiId, username)) {
                    System.out.println("RTI Request deleted.");
                } else {
                    System.out.println("Error: Could not find request.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
        }
    }
}