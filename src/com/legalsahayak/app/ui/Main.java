package com.legalsahayak.app.ui;

import com.legalsahayak.app.db.DatabaseService;
import com.legalsahayak.app.model.UserSession; // Import session
import com.legalsahayak.app.service.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // --- Initialization ---
        DatabaseService dbService = new DatabaseService();
        dbService.connect();
    
        AuthService authService = new AuthService(scanner, dbService);
        RightsNavigatorService navService = 
            new RightsNavigatorService(scanner, dbService);
        EvidenceLogService logService = 
            new EvidenceLogService(scanner, dbService);
        QuizService quizService = new QuizService(scanner, dbService);
        RtiService rtiService = new RtiService(scanner, dbService);
        JargonBusterService jargonService = 
            new JargonBusterService(scanner, dbService);
        // NEW: Use the new AdminService
        AdminService adminService = 
            new AdminService(scanner, dbService);

        UIHandler.displayWelcomeMessage();
        
        // --- Authentication Loop ---
        UserSession session = null;
        while (session == null) {
            UIHandler.displayAuthMenu();
            String authChoice = scanner.nextLine();
            
            switch (authChoice) {
                case "1": session = authService.login(); break;
                case "2": authService.register(); break;
                case "3":
                    UIHandler.displayExitMessage();
                    scanner.close();
                    return; // Exit program
                default: System.out.println("Invalid choice.");
            }
        }
        
        // --- Main App Loop ---
        System.out.println("\nWelcome, " + session.getUsername() + "!");
        boolean isAppRunning = true;
        int exitOptionId = dbService.getExitOptionId();

        while (isAppRunning) {
            UIHandler.displayMainMenu(dbService);
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                // Handled by default case in handleMenuChoice
            }

            if (choice == exitOptionId) {
                isAppRunning = false;
            } else {
                // Pass the session to handle admin checks
                handleMenuChoice(choice, session, navService, 
                    logService, quizService, rtiService, 
                    jargonService, adminService); // Pass new adminService
            }
        }
        
        UIHandler.displayExitMessage();
        scanner.close();
    }

    /** Handles the main menu choice, including role-based security. */
    private static void handleMenuChoice(int choice, UserSession session,
                                         RightsNavigatorService nav,
                                         EvidenceLogService log,
                                         QuizService quiz,
                                         RtiService rti,
                                         JargonBusterService jargon,
                                         AdminService admin) { // Use AdminService
        
        String user = session.getUsername(); // For user-specific CRUD

        switch (choice) {
            case 1: nav.start(); break;         // 1. Navigator
            case 2: log.start(user); break;  // 2. Evidence Log
            case 3: quiz.start(); break;         // 3. Quiz
            case 4: rti.start(user); break;   // 4. RTI
            case 5: jargon.start(); break;       // 5. Jargon
            
            case 6: // 6. Admin Menu
                if (session.isAdmin()) {
                    admin.start(); // Call the new Admin Service
                } else {
                    System.out.println("Access denied. Admin only.");
                }
                break;
                
            default: System.out.println("Invalid choice. Please try again.");
        }
    }
}