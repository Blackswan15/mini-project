package com.legalsahayak.app.model;

public class QuizCard {
    private final String question;
    private final String[] options;
    private final int correctAnswerIndex;
    private final String explanation;
    private final String topic; // <-- ADD THIS

    public QuizCard(String q, String[] o, int a, String e, String t) { // <-- ADD 't'
        this.question = q;
        this.options = o;
        this.correctAnswerIndex = a;
        this.explanation = e;
        this.topic = t; // <-- ADD THIS
    }

    public String getQuestion() { return question; }
    public String[] getOptions() { return options; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public String getExplanation() { return explanation; }
    public String getTopic() { return topic; } // <-- ADD THIS
}