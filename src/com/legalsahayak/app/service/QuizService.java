package com.legalsahayak.app.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import com.legalsahayak.app.model.QuizCard;

public class QuizService {
    private final ArrayList<QuizCard> quizCards = new ArrayList<>();
    private final Scanner scanner;
    private static final int QUESTIONS_IN_QUIZ = 5;

    public QuizService(Scanner scanner) {
        this.scanner = scanner;
        initializeQuiz();
    }

    public void start() {
        System.out.println("\n--- 🤔 Legal Assessment Quiz ---");
        System.out.println("You will be asked " + QUESTIONS_IN_QUIZ + " questions. Let's begin!");
        
        Collections.shuffle(quizCards);
        List<QuizCard> quizSelection = quizCards.subList(0, QUESTIONS_IN_QUIZ);
        
        int score = 0;
        Set<String> topicsToReview = new HashSet<>();

        for (int i = 0; i < quizSelection.size(); i++) {
            QuizCard card = quizSelection.get(i);
            System.out.println("\nQuestion " + (i + 1) + ": " + card.getQuestion());
            
            String[] options = card.getOptions();
            for (int j = 0; j < options.length; j++) {
                System.out.println((j + 1) + ". " + options[j]);
            }
            
            System.out.print("Your answer: ");
            if (isAnswerCorrect(card)) {
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

    private boolean isAnswerCorrect(QuizCard card) {
        try {
            int userAnswer = Integer.parseInt(scanner.nextLine());
            return userAnswer - 1 == card.getCorrectAnswerIndex();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void displayResults(int score, Set<String> topicsToReview) {
        System.out.println("\n--- Quiz Complete! ---");
        System.out.println("Your final score: " + score + " out of " + QUESTIONS_IN_QUIZ);

        if (score == QUESTIONS_IN_QUIZ) {
            System.out.println("Excellent work! You have a strong understanding of your rights.");
        } else {
            System.out.println("\nTo improve your knowledge, we suggest you revisit these topics in the 'Legal Rights Navigator':");
            for (String topic : topicsToReview) {
                System.out.println("- " + topic);
            }
        }
    }

    private void initializeQuiz() {
        // Questions now include a topic at the end
        quizCards.add(new QuizCard("Can a woman file a 'Zero FIR' at any police station...", new String[]{"Yes", "No"}, 0, "Explanation: Yes. A 'Zero FIR' ensures immediate action...", "Domestic Issues"));
        quizCards.add(new QuizCard("Is a woman required to go to the police station to give her statement...", new String[]{"Yes", "No"}, 1, "Explanation: No. A woman has the right to have her statement recorded at her residence...", "Domestic Issues"));
        quizCards.add(new QuizCard("Does a daughter have the same right to her father's ancestral property as a son?", new String[]{"Yes", "No"}, 0, "Explanation: Yes. The Hindu Succession Act grants daughters equal rights...", "Property Rights"));
        quizCards.add(new QuizCard("Is demanding gifts from the bride's family after marriage a dowry crime?", new String[]{"Yes", "No"}, 0, "Explanation: Yes. Any demand for property in connection with a marriage is illegal...", "Dowry Issues"));
        quizCards.add(new QuizCard("If someone posts your private photos online without permission, is it a crime?", new String[]{"Yes", "No"}, 0, "Explanation: Yes. This is a cybercrime. You can file a complaint at cybercrime.gov.in.", "Cybercrime Issues"));
        quizCards.add(new QuizCard("Can an employer legally pay a woman less than a man for the same work?", new String[]{"Yes", "No"}, 1, "Explanation: No. The Equal Remuneration Act, 1976, mandates equal pay for equal work.", "Workplace Issues"));
        quizCards.add(new QuizCard("Does a company with 15 employees need an Internal Committee (IC) for harassment?", new String[]{"Yes", "No"}, 0, "Explanation: Yes. The PoSH Act requires any workplace with 10 or more employees to have an IC.", "Workplace Issues"));
    }
}