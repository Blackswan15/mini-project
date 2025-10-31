package com.legalsahayak.app.service;

import com.legalsahayak.app.db.DatabaseService;
import com.legalsahayak.app.model.UserSession; // Import new model
import java.util.Map; // Using Map data structure
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private final Scanner scanner;
    private final DatabaseService dbService;

    public AuthService(Scanner scanner, DatabaseService dbService) {
        this.scanner = scanner;
        this.dbService = dbService;
    }

    /**
     * Handles user login.
     * @return A UserSession object if login is successful, null otherwise.
     */
    public UserSession login() {
        System.out.println("\n--- Login ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        Map<String, String> details = dbService.getLoginDetails(username);

        if (details == null) {
            System.out.println("Error: User not found.");
            return null;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (BCrypt.checkpw(password, details.get("hash"))) {
            System.out.println("Login successful.");
            return new UserSession(username, details.get("role"));
        } else {
            System.out.println("Error: Invalid password.");
            return null;
        }
    }
    
   
    public void register() {
        System.out.println("\n--- Register ---");
        System.out.print("Enter new username: ");
        String username = scanner.nextLine();

        if (dbService.doesUserExist(username)) {
            System.out.println("Error: Username already exists.");
            return;
        }

        System.out.print("Enter new password: ");
        String password = scanner.nextLine();
        
        // Hash the password for security
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        
        if (dbService.registerUser(username, hashedPassword)) {
            System.out.println("Registration successful. Please login.");
        } else {
            System.out.println("Error: Registration failed.");
        }
    }
}