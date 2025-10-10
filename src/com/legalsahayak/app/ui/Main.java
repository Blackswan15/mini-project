package com.legalsahayak.app.ui;

import java.util.Scanner;
import com.legalsahayak.app.service.EvidenceLogService;
import com.legalsahayak.app.service.QuizService;
import com.legalsahayak.app.service.RightsNavigatorService;
import com.legalsahayak.app.service.RtiService; // <-- ADDED

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Initialize all the services
        RightsNavigatorService rightsNavigator = new RightsNavigatorService(scanner);
        EvidenceLogService evidenceLog = new EvidenceLogService(scanner);
        QuizService quizService = new QuizService(scanner);
        RtiService rtiService = new RtiService(scanner); // <-- ADDED

        UIHandler.displayWelcomeMessage();
        
        boolean isRunning = true;
        while (isRunning) {
            UIHandler.displayMainMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": rightsNavigator.start(); break;
                case "2": evidenceLog.addEntry(); break;
                case "3": evidenceLog.viewEntries(); break;
                case "4": quizService.start(); break;
                case "5": rtiService.start(); break; // <-- ADDED
                case "6": isRunning = false; break; // <-- UPDATED
                default: System.out.println("Invalid choice. Please try again."); break;
            }
        }
        
        UIHandler.displayExitMessage();
        scanner.close();
    }
}