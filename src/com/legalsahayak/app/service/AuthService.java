package com.legalsahayak.app.service;

import com.legalsahayak.app.db.DatabaseService;
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final Scanner scanner;
    private final DatabaseService dbService;

    public AuthService(Scanner scanner, DatabaseService dbService) {
        this.scanner = scanner;
        this.dbService = dbService;
    }

    public boolean register() {
        System.out.println("\n--- New User Registration ---");
        System.out.print("Enter a new username: ");
        String username = scanner.nextLine();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return false;
        }

        if (dbService.doesUserExist(username)) {
            System.out.println("Error: This username is already taken.");
            return false;
        }

        System.out.print("Enter a password (min 4 chars): ");
        String password = scanner.nextLine();
        if (password.length() < 4) {
            System.out.println("Password is too short.");
            return false;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // --- THIS IS THE FIX ---
        // Check if the database write was successful
        boolean success = dbService.registerUser(username, hashedPassword);

        if (success) {
            System.out.println("Registration successful! Please log in.");
            return true;
        } else {
            return false;
        }
    }

    public String login() {
        System.out.println("\n--- User Login ---");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        String storedHash = dbService.getUserPasswordHash(username);

        if (storedHash == null) {
            System.out.println("Error: Invalid username or password.");
            return null; // User does not exist
        }

        if (BCrypt.checkpw(password, storedHash)) {
            System.out.println("Login successful!");
            return username; // Success
        } else {
            System.out.println("Error: Invalid username or password.");
            return null; // Password was incorrect
        }
    }
}