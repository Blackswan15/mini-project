package com.legalsahayak.app.service;

import com.legalsahayak.app.model.LawyerProfile;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RightsNavigatorService {
    private final Map<Integer, String> categories = new HashMap<>();
    private final Map<String, LawyerProfile> lawyerDatabase = new HashMap<>();
    private final Scanner scanner;

    public RightsNavigatorService(Scanner scanner) {
        this.scanner = scanner;
        initializeCategories();
        initializeLawyers();
    }

    public void start() {
        System.out.println("\n--- ⚖️ Legal Rights Navigator ---");
        System.out.println("Select a category to learn more:");
        categories.forEach((k, v) -> System.out.println(k + ". " + v));
        System.out.print("Your choice: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            String category = categories.get(choice);
            if (category == null) { System.out.println("Invalid category."); return; }
            
            String tagPrefix = category.replace(" ", "_").toUpperCase();
            System.out.println("\n--- Definition ---\n" + getContentByTag("#" + tagPrefix + "_DEFINITION"));

            System.out.print("\nSee relevant laws? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                System.out.println("\n--- Relevant Laws ---\n" + getContentByTag("#" + tagPrefix + "_LAWS"));
                
                System.out.print("\nFind a simulated lawyer for this issue? (y/n): ");
                if (scanner.nextLine().equalsIgnoreCase("y")) {
                    displayLawyerProfile(category);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    // Replace the existing displayLawyerProfile() method with this one.

private void displayLawyerProfile(String topic) {
    System.out.println("\n--- Simulated Lawyer Profiles for: " + topic + " ---");
    boolean found = false;
    for (Map.Entry<String, LawyerProfile> entry : lawyerDatabase.entrySet()) {
        if (entry.getKey().startsWith(topic)) {
            System.out.println("----------------------------------------");
            System.out.println(entry.getValue().toString());
            found = true;
        }
    }
    if (!found) {
        System.out.println("No specialized lawyers found for this topic.");
    }
    System.out.println("----------------------------------------");
}

    private String getContentByTag(String tag) {
        StringBuilder content = new StringBuilder();
        boolean isReading = false;
        try (BufferedReader br = new BufferedReader(new FileReader("laws.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals(tag)) isReading = true;
                else if (line.trim().equals("#END")) isReading = false;
                else if (isReading) content.append(line).append("\n");
            }
        } catch (IOException e) {
            return "Error: Could not read laws.txt.";
        }
        return content.toString();
    }

    private void initializeCategories() {
        categories.put(1, "Domestic Issues");
        categories.put(2, "Workplace Issues");
        categories.put(3, "Dowry Issues");
        categories.put(4, "Property Rights");
        categories.put(5, "Cybercrime Issues");
        categories.put(6, "Maternity Benefits");
        categories.put(7, "Right to Privacy");
    }
    
    // Replace the existing initializeLawyers() method in this file with the one below.

private void initializeLawyers() {
    // Lawyers for Domestic Issues
    lawyerDatabase.put("Domestic Issues", new LawyerProfile("Adv. Priya Sharma", "Family Law & Domestic Violence", "Specializes in securing protection orders.", "98765-43210", "priya.sharma.law@email.com"));
    lawyerDatabase.put("Domestic Issues_2", new LawyerProfile("Adv. Sunita Singh", "Family Law", "Experienced in handling cases of mental and emotional cruelty.", "98877-66554", "sunita.singh.legal@email.com"));
    lawyerDatabase.put("Domestic Issues_3", new LawyerProfile("Adv. Lakshmi Menon", "Women's Rights Litigation", "Focuses on securing monetary relief and maintenance for victims.", "91234-56780", "l.menon.adv@email.com"));
    lawyerDatabase.put("Domestic Issues_4", new LawyerProfile("Adv. Fatima Ali", "Criminal & Family Law", "Handles cases involving Section 498A and related criminal charges.", "99889-98899", "fatima.ali.law@email.com"));

    // Lawyers for Workplace Issues
    lawyerDatabase.put("Workplace Issues", new LawyerProfile("Adv. Anjali Verma", "Labor & Employment Law", "Expert in PoSH Act compliance and IC representation.", "91234-56789", "anjali.verma.legal@email.com"));
    lawyerDatabase.put("Workplace Issues_2", new LawyerProfile("Adv. Rohan Desai", "Corporate Law", "Advises companies on setting up Internal Committees and conducting workshops.", "98765-11223", "rohan.desai.corp@email.com"));
    lawyerDatabase.put("Workplace Issues_3", new LawyerProfile("Adv. Sneha Reddy", "Employment Litigation", "Represents employees in cases of wrongful termination and workplace discrimination.", "95556-67778", "snehareddy.law@email.com"));
    lawyerDatabase.put("Workplace Issues_4", new LawyerProfile("Adv. Karan Mehta", "Labor Rights", "Focuses on cases related to the Equal Remuneration Act and wage disputes.", "91122-33445", "karan.mehta.adv@email.com"));
    
    // Lawyers for Dowry Issues
    lawyerDatabase.put("Dowry Issues", new LawyerProfile("Adv. Geetha Rajan", "Criminal Law", "Specializes in cases under the Dowry Prohibition Act and Section 304B (Dowry Death).", "98765-43211", "geetha.rajan.law@email.com"));
    lawyerDatabase.put("Dowry Issues_2", new LawyerProfile("Adv. Savita Kumari", "Family & Criminal Law", "Experienced in handling dowry harassment and cruelty cases under Section 498A.", "99988-87776", "savita.k.adv@email.com"));
    lawyerDatabase.put("Dowry Issues_3", new LawyerProfile("Adv. Imran Khan", "Litigation", "Focuses on the recovery of 'Stridhan' and property in dowry disputes.", "91234-12345", "imran.khan.legal@email.com"));
    lawyerDatabase.put("Dowry Issues_4", new LawyerProfile("Adv. Deepa Iyer", "Women's Rights Law", "Provides legal aid and representation for victims of dowry-related crimes.", "98888-77777", "deepa.iyer.law@email.com"));

    // Lawyers for Property Rights
    lawyerDatabase.put("Property Rights", new LawyerProfile("Adv. Meera Gupta", "Property & Inheritance Law", "Focuses on helping women claim their share in ancestral property.", "99887-76655", "meera.gupta.adv@email.com"));
    lawyerDatabase.put("Property Rights_2", new LawyerProfile("Adv. Rajesh Kumar", "Civil Law", "Handles property disputes and succession cases under the Hindu Succession Act.", "97766-55443", "rajesh.kumar.civillaw@email.com"));
    lawyerDatabase.put("Property Rights_3", new LawyerProfile("Adv. Anita Rao", "Inheritance Law", "Specializes in cases related to a widow's right to her husband's property.", "96655-44332", "anita.rao.advocate@email.com"));
    lawyerDatabase.put("Property Rights_4", new LawyerProfile("Adv. Vikram Singh", "Real Estate Law", "Expert in matters of self-acquired property and writing wills.", "95544-33221", "vikram.singh.law@email.com"));

    // Lawyers for Cybercrime Issues
    lawyerDatabase.put("Cybercrime Issues", new LawyerProfile("Adv. Siddharth Joshi", "Cyber Law & IT Act", "Specializes in cases of online harassment, stalking, and defamation.", "98765-98765", "sid.joshi.cyberlaw@email.com"));
    lawyerDatabase.put("Cybercrime Issues_2", new LawyerProfile("Adv. Neha Sharma", "Technology Law", "Handles cases involving revenge porn and violation of privacy under the IT Act.", "91234-98765", "neha.sharma.techlaw@email.com"));
    lawyerDatabase.put("Cybercrime Issues_3", new LawyerProfile("Adv. Arjun Patel", "Criminal Law (Cyber)", "Focuses on filing complaints with the Cyber Cell and legal action against online abusers.", "99999-88888", "arjun.patel.law@email.com"));
    lawyerDatabase.put("Cybercrime Issues_4", new LawyerProfile("Adv. Preeti Singh", "Intellectual Property & Privacy Law", "Provides consultation on how to deal with online impersonation and morphing.", "98877-88990", "preeti.singh.legal@email.com"));
    }
}