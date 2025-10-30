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
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n---  RTI Request Manager ---");
            System.out.println("1. Generate New RTI Request");
            System.out.println("2. View My RTI Requests");
            System.out.println("3. Update RTI Status");
            System.out.println("4. Back to Main Menu");
            System.out.print("Your choice: ");
            
            switch (scanner.nextLine()) {
                case "1": generateNewRTI(username); break;
                case "2": dbService.displayUserRTIRequests(username); break;
                case "3": updateStatus(username); break;
                case "4": isRunning = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void generateNewRTI(String username) {
        System.out.println("\n--- New RTI Request Generator ---");
        String authority = getUserInput("[1] Public Authority (e.g., Municipal Corp): ");
        String infoNeeded = getUserInput("[2] Specific information needed: ");
        String fullName = getUserInput("[3] Your full name: ");
        String address = getUserInput("[4] Your full address: ");

        String rtiText = generateRtiText(authority, infoNeeded, fullName, address);
        dbService.saveRTIRequest(username, authority, infoNeeded, fullName, address, rtiText);

        System.out.println("\n--- RTI Application Saved! ---");
        System.out.println(rtiText);
    }

    private void updateStatus(String username) {
        System.out.println("\n--- Update RTI Status ---");
        if (!dbService.displayUserRTIRequests(username)) {
            return; // Don't proceed if there are no RTIs
        }
        
        System.out.print("Enter the ID of the RTI to update: ");
        int rtiId = -1;
        try {
            rtiId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
            return;
        }

        System.out.print("Enter new status (e.g., 'Submitted', 'Replied'): ");
        String newStatus = scanner.nextLine();
        if (newStatus.isEmpty()) {
            System.out.println("Status cannot be empty.");
            return;
        }
        
        dbService.updateRTIStatus(rtiId, newStatus, username);
    }

    private String getUserInput(String prompt) {
        System.out.println(prompt);
        System.out.print("> ");
        return scanner.nextLine();
    }

    private String generateRtiText(String auth, String info, String name, String addr) {
        return String.format(
            "----------------------------------------------------\n" +
            "To,\nThe Public Information Officer (PIO),\n%s\n\n" +
            "Subject: Request for Information under RTI Act, 2005\n\n" +
            "Sir/Madam,\nI, %s, residing at %s, seek the following info:\n" +
            "1. %s\n\n" +
            "Sincerely,\n%s\n" +
            "----------------------------------------------------\n",
            auth, name, addr, info, name
        );
    }
}