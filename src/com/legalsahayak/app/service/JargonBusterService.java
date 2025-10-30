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

    public void start() {
        System.out.println("\n--- 📖 Legal Jargon Buster ---");
        System.out.print("Enter a legal term (e.g., FIR): ");
        String term = scanner.nextLine();

        if (term.isEmpty()) {
            System.out.println("No term entered.");
            return;
        }

        String definition = dbService.getJargonDefinition(term);
        
        if (definition != null) {
            System.out.println("\nDefinition of " + term.toUpperCase() + ":");
            System.out.println(definition);
        } else {
            System.out.println("Sorry, that term was not found.");
        }
    }
}