package com.legalsahayak.app.ui.panels;

import com.legalsahayak.app.db.IDatabaseService;
import com.legalsahayak.app.model.QuizCard;
import com.legalsahayak.app.ui.BasePanel;
import com.legalsahayak.app.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;


public class QuizPanel extends BasePanel {
    private final JLabel questionLabel;
    private final ButtonGroup optionsGroup;
    private final JRadioButton[] optionButtons;
    private final JButton submitButton;
    private final JTextArea explanationArea;
    
    private List<QuizCard> questions;
    private int currentQuestionIndex;
    private int score;
    private QuizCard currentCard;
    private boolean answerSubmitted;

    public QuizPanel(MainFrame mainFrame, IDatabaseService dbService) {
        super(mainFrame, dbService);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Legal Quiz", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> mainFrame.switchPanel("MainMenu"));
        topPanel.add(backButton, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        JPanel quizPanel = new JPanel();
        quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS)); // Stack vertically
        
        questionLabel = new JLabel("Q: Question will appear here");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        quizPanel.add(questionLabel);
        
        quizPanel.add(Box.createVerticalStrut(15)); 

        optionsGroup = new ButtonGroup();
        optionButtons = new JRadioButton[4]; // Assume max 4 options
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new JRadioButton("Option " + (i + 1));
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 14));
            optionsGroup.add(optionButtons[i]);
            quizPanel.add(optionButtons[i]);
        }
        
        quizPanel.add(Box.createVerticalStrut(15));

        submitButton = new JButton("Submit Answer");
        quizPanel.add(submitButton);

        explanationArea = new JTextArea(5, 20);
        explanationArea.setEditable(false);
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setFont(new Font("Arial", Font.ITALIC, 14));
        explanationArea.setBackground(getBackground()); // Match panel background
        quizPanel.add(Box.createVerticalStrut(15));
        quizPanel.add(new JScrollPane(explanationArea));

        add(quizPanel, BorderLayout.CENTER);

        submitButton.addActionListener(e -> handleSubmit());

        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent e) {
                startQuiz(); 
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent e) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent e) {}
        });
    }

    private void startQuiz() {
        questions = dbService.getQuizQuestions(5); 
        Collections.shuffle(questions);
        currentQuestionIndex = 0;
        score = 0;
        answerSubmitted = false; 
        
        if (questions.isEmpty()) {
            questionLabel.setText("No quiz questions found in the database.");
            submitButton.setEnabled(false);
            for(JRadioButton btn : optionButtons) btn.setVisible(false);
            explanationArea.setText("");
            return;
        }
        
        loadQuestion();
    }

    private void loadQuestion() {
        currentCard = questions.get(currentQuestionIndex);
        
        questionLabel.setText("Q: " + currentCard.getQuestion());
        
        String[] options = currentCard.getOptions();
        for (int i = 0; i < optionButtons.length; i++) {
            if (i < options.length) {
                optionButtons[i].setText(options[i]);
                optionButtons[i].setVisible(true);
                optionButtons[i].setEnabled(true); 
            } else {
                optionButtons[i].setVisible(false); 
            }
        }
        
        optionsGroup.clearSelection(); 
        explanationArea.setText("");
        explanationArea.setBackground(getBackground());
        submitButton.setText("Submit Answer");
        submitButton.setEnabled(true);
        answerSubmitted = false; // We are now waiting for a new answer
    }

   
    private void handleSubmit() {
        // CASE 1: This was a "Next Question" or "Finish Quiz" click 
        if (answerSubmitted) {
            currentQuestionIndex++; // Move to the next question index
            
            if (currentQuestionIndex < questions.size()) {
                loadQuestion(); // Load the next question
            } else {
                showFinalScore(); // Show score and restart
            }
            return; 
        }

        //  CASE 2: This was a "Submit Answer" click 
        int selectedAnswer = -1;
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                selectedAnswer = i + 1;
                break;
            }
        }

        if (selectedAnswer == -1) {
            JOptionPane.showMessageDialog(this, "Please select an answer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        answerSubmitted = true; 
        submitButton.setEnabled(true); 
        
        for (JRadioButton btn : optionButtons) {
            btn.setEnabled(false);
        }

        String explanation;
        if (selectedAnswer == currentCard.getCorrectOptionIndex()) {
            score++;
            explanation = "Correct! \n" + currentCard.getExplanation();
            explanationArea.setForeground(new Color(0, 128, 0)); // Dark green
        } else {
            explanation = "Incorrect. \n" + currentCard.getExplanation();
            explanationArea.setForeground(Color.RED);
        }
        
        explanationArea.setText(explanation);
        
        if (currentQuestionIndex == questions.size() - 1) { // This was the last question
            submitButton.setText("Finish Quiz");
        } else {
            submitButton.setText("Next Question");
        }
    }
    
    private void showFinalScore() {
        JOptionPane.showMessageDialog(this, 
            "Quiz finished! Your score: " + score + " / " + questions.size(), 
            "Quiz Complete", 
            JOptionPane.INFORMATION_MESSAGE);
        startQuiz(); 
    }
}