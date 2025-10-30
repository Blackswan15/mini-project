package com.legalsahayak.app.service;

import com.legalsahayak.app.db.DatabaseService;
import com.legalsahayak.app.model.QuizCard;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class QuizService {
    
    private final Scanner scanner;
    private final DatabaseService dbService;
    private static final int QUESTIONS_IN_QUIZ = 5;

    public QuizService(Scanner scanner, DatabaseService dbService) {
        this.scanner = scanner;
        this.dbService = dbService;
    }

    public void start() {
        System.out.println("\n--- 🤔 Legal Assessment Quiz ---");
        List<QuizCard> quizSelection = dbService.getQuizQuestions(QUESTIONS_IN_QUIZ);

        if (quizSelection.isEmpty()) {
            System.out.println("Could not load quiz questions.");
            return;
        }
        
        int score = 0;
        Set<String> topicsToReview = new HashSet<>();

        for (int i = 0; i < quizSelection.size(); i++) {
            QuizCard card = quizSelection.get(i);
            System.out.println("\nQuestion " + (i + 1) + ": " + card.getQuestion());
            
            String[] options = card.getOptions();
            for (int j = 0; j < options.length; j++) {
                System.out.println((j + 1) + ". " + options[j]);
            }
            
            int answer = getUserAnswer();
            if (answer - 1 == card.getCorrectAnswerIndex()) {
                System.out.println("✅ Correct!");
                score++;
            } else {
                System.out.println("❌ Incorrect.");
                topicsToReview.add(card.getTopic());
            }
            System.out.println(card.getExplanation());
        }
        displayResults(score, topicsToReview);
    }

    private int getUserAnswer() {
        System.out.print("Your answer: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Invalid answer
        }
    }

    private void displayResults(int score, Set<String> topics) {
        System.out.println("\n--- Quiz Complete! ---");
        System.out.println("Your final score: " + score + " out of " + QUESTIONS_IN_QUIZ);

        if (score == QUESTIONS_IN_QUIZ) {
            System.out.println("Excellent work! You have a strong understanding.");
        } else if (!topics.isEmpty()) {
            System.out.println("\nTopics to review in the 'Rights Navigator':");
            for (String topic : topics) {
                System.out.println("- " + topic);
            }
        }
    }
}