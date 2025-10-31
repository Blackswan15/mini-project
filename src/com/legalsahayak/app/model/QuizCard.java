package com.legalsahayak.app.model;


public class QuizCard {
    private final String question;
    private final String[] options;
    private final int correctOptionIndex;
    private final String explanation;
    private final String category;

    public QuizCard(String q, String[] opts, int correctIdx, 
                    String exp, String cat) {
        this.question = q;
        this.options = opts;
        this.correctOptionIndex = correctIdx;
        this.explanation = exp;
        this.category = cat;
    }

    // Getters
    public String getQuestion() { return question; }
    public String[] getOptions() { return options; }
    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public String getExplanation() { return explanation; }
    public String getCategory() { return category; }
}