package com.legalsahayak.app.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class JargonBusterService {
    private final Map<String, String> dictionary = new HashMap<>();
    private final Scanner scanner;

    public JargonBusterService(Scanner scanner) {
        this.scanner = scanner;
        // Initialize with sample data
        dictionary.put("FIR", "First Information Report. A document prepared by the police when they receive information about a crime.");
        dictionary.put("Affidavit", "A written statement confirmed by oath or affirmation, for use as evidence in court.");
        dictionary.put("Bail", "The temporary release of an accused person awaiting trial, sometimes on condition that a sum of money be lodged to guarantee their appearance in court.");
    }

    public void start() {
        System.out.println("\n--- 📖 Legal Jargon Buster ---");
        System.out.print("Enter a legal term to look up (e.g., FIR): ");
        String term = scanner.nextLine().toUpperCase();

        String definition = dictionary.get(term);
        if (definition != null) {
            System.out.println("\nDefinition of " + term + ":");
            System.out.println(definition);
        } else {
            System.out.println("Sorry, that term was not found.");
        }
    }
}