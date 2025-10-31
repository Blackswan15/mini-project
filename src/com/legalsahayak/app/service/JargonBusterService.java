package com.legalsahayak.app.service;

import com.legalsahayak.app.db.DatabaseService;
import java.util.Scanner;

public class JargonBusterService {
    private final Scanner scanner;
    private final DatabaseService dbService;

    public JargonBusterService(Scanner scanner, DatabaseService dbService) {
        this.scanner = scanner;
        this.dbService = dbService;
    }

    /**
     * Simple service to look up terms in the local database.
     */
    public void start() {
        System.out.println("\n--- Jargon Buster ---");
        System.out.print("Enter a legal term (or 'exit' to quit): ");
        String term = scanner.nextLine();
        
        if (term.equalsIgnoreCase("exit")) return;

        // Search the local database
        // The term is converted to uppercase in the DB service
        String def = dbService.getJargonDefinition(term);
        
        if (def != null) {
            System.out.println("\nDefinition:\n" + def);
        } else {
            System.out.println("Term not found in our database.");
            System.out.println("Admins can add new terms to the database.");
        }
    }
}