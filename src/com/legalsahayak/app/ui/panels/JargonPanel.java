package com.legalsahayak.app.ui.panels;

import com.legalsahayak.app.db.IDatabaseService;
import com.legalsahayak.app.ui.BasePanel;
import com.legalsahayak.app.ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class JargonPanel extends BasePanel {

    private final JTextField termField;
    private final JTextArea definitionArea;

    public JargonPanel(MainFrame mainFrame, IDatabaseService dbService) {
        super(mainFrame, dbService);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Jargon Buster", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> mainFrame.switchPanel("MainMenu"));
        topPanel.add(backButton, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Enter Term:"));
        termField = new JTextField(20);
        searchPanel.add(termField);
        
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        
        add(searchPanel, BorderLayout.CENTER);

        definitionArea = new JTextArea(10, 40);
        definitionArea.setEditable(false);
        definitionArea.setLineWrap(true);
        definitionArea.setWrapStyleWord(true);
        definitionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        definitionArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(definitionArea);
        add(scrollPane, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> searchJargon());
        termField.addActionListener(e -> searchJargon()); 
    }

    private void searchJargon() {
        String term = termField.getText();
        if (term.isBlank()) {
            definitionArea.setText("Please enter a term to search.");
            return;
        }

        String definition = dbService.getJargonDefinition(term);
        
        if (definition != null) {
            definitionArea.setText(definition);
        } else {
            definitionArea.setText("Term '" + term + "' not found in our database.");
        }
        definitionArea.setCaretPosition(0); 
    }
}