package com.legalsahayak.app.service;

import com.legalsahayak.app.db.DatabaseService;
import com.legalsahayak.app.model.QuizCard;
import java.util.List;
import java.util.Scanner;

public class QuizService {
    private final Scanner scanner;
    private final DatabaseService dbService;

    public QuizService(Scanner scanner, DatabaseService dbService) {
        this.scanner = scanner;
        this.dbService = dbService;
    }

    public void start() {
        System.out.println("\n--- Legal Rights Quiz ---");
        List<QuizCard> questions = dbService.getQuizQuestions(5); // Get 5
        int score = 0;

        for (QuizCard card : questions) {
            System.out.println("\nCategory: " + card.getCategory());
            System.out.println("Q: " + card.getQuestion());
            
            String[] options = card.getOptions();
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            
            System.out.print("Enter your answer (1-" + options.length + "): ");
            int userAnswer = -1;
            try {
                userAnswer = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                // Handle invalid number input
            }
            
            if (userAnswer == card.getCorrectOptionIndex()) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Incorrect.");
            }
            System.out.println("Explanation: " + card.getExplanation());
        }
        
        System.out.println("\n--- Quiz Finished ---");
        System.out.println("Your score: " + score + " / " + questions.size());
    }
}