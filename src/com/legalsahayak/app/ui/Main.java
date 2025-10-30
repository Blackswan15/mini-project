package com.legalsahayak.app.ui;

import com.legalsahayak.app.db.DatabaseService;
import com.legalsahayak.app.service.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        DatabaseService dbService = new DatabaseService();
        dbService.connect();
    
        AuthService authService = new AuthService(scanner, dbService);
        RightsNavigatorService navService = new RightsNavigatorService(scanner, dbService);
        EvidenceLogService logService = new EvidenceLogService(scanner, dbService);
        QuizService quizService = new QuizService(scanner, dbService);
        RtiService rtiService = new RtiService(scanner, dbService);
        JargonBusterService jargonService = new JargonBusterService(scanner, dbService);

        UIHandler.displayWelcomeMessage();
        
        String loggedInUsername = null;
        while (loggedInUsername == null) {
            UIHandler.displayAuthMenu();
            String authChoice = scanner.nextLine();
            
            switch (authChoice) {
                case "1": loggedInUsername = authService.login(); break;
                case "2": authService.register(); break;
                case "3":
                    UIHandler.displayExitMessage();
                    scanner.close();
                    return; // Exit program
                default: System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
        
        System.out.println("\nWelcome, " + loggedInUsername + "!");
        boolean isAppRunning = true;
        int exitOptionId = dbService.getExitOptionId();

        while (isAppRunning) {
            UIHandler.displayMainMenu(dbService);
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                // Handled by default case
            }

            if (choice == exitOptionId) {
                isAppRunning = false;
            } else {
                handleMenuChoice(choice, loggedInUsername, navService, logService, quizService, rtiService, jargonService);
            }
        }
        
        UIHandler.displayExitMessage();
        scanner.close();
    }
 
    private static void handleMenuChoice(int choice, String user,
                                         RightsNavigatorService nav,
                                         EvidenceLogService log,
                                         QuizService quiz,
                                         RtiService rti,
                                         JargonBusterService jargon) {
        switch (choice) {
            case 1: nav.start(); break;
            case 2: log.addEntry(user); break;
            case 3: log.viewEntries(user); break;
            case 4: quiz.start(); break;
            case 5: rti.start(user); break;
            case 6: jargon.start(); break;
            default: System.out.println("Invalid choice. Please try again.");
        }
    }
}