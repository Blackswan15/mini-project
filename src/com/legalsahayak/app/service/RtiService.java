package com.legalsahayak.app.service;

import java.util.Scanner;

public class RtiService {
    private final Scanner scanner;

    public RtiService(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        System.out.println("\n---  RTI Request Generator ---");
        System.out.println("This tool will help you create an application under the RTI Act, 2005.");

        // Gather information from the user
        String authority = getUserInput("[1] Enter the Public Authority (e.g., Municipal Corporation): ");
        String infoNeeded = getUserInput("[2] Enter the specific information you need: ");
        String fullName = getUserInput("[3] Enter your full name: ");
        String address = getUserInput("[4] Enter your full address: ");

        // Generate and display the RTI text
        String rtiApplication = generateRtiText(authority, infoNeeded, fullName, address);
        System.out.println("\n--- Generating Your RTI Application ---");
        System.out.println("Your RTI application is ready. Copy this, print it, and submit it to the relevant office with the required fee (usually Rs. 10).");
        System.out.println(rtiApplication);
    }

    private String getUserInput(String prompt) {
        System.out.println(prompt);
        System.out.print("> ");
        return scanner.nextLine();
    }

    private String generateRtiText(String authority, String info, String name, String addr) {
        return """
        ----------------------------------------------------
        To,
        The Public Information Officer (PIO),
        %s

        Subject: Request for Information under the RTI Act, 2005

        Sir/Madam,

        I, %s, a citizen of India, residing at %s, seek the following information under the Right to Information Act:

        1. %s

        Please provide the information in a timely manner as prescribed under the RTI Act.

        Sincerely,
        %s
        ----------------------------------------------------
        """.formatted(authority, name, addr, info, name);
    }
}