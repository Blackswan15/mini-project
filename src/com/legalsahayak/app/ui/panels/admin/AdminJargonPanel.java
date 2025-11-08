
package com.legalsahayak.app.ui.panels.admin; 

import com.legalsahayak.app.db.IDatabaseService;
import com.legalsahayak.app.ui.BasePanel;
import com.legalsahayak.app.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
public class AdminJargonPanel extends BasePanel {

    private final JTextField termField;
    private final JTextArea definitionArea;
    private final JTextField deleteTermField;

    public AdminJargonPanel(MainFrame mainFrame, IDatabaseService dbService) {
        super(mainFrame, dbService);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel addPanel = new JPanel(new BorderLayout(5, 5));
        addPanel.setBorder(BorderFactory.createTitledBorder("Add / Update Jargon Term"));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Term:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; termField = new JTextField(25); formPanel.add(termField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Definition:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; definitionArea = new JTextArea(5, 25);
        definitionArea.setLineWrap(true);
        definitionArea.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(definitionArea), gbc);
        
        addPanel.add(formPanel, BorderLayout.CENTER);
        
        JButton saveButton = new JButton("Save Term");
        addPanel.add(saveButton, BorderLayout.SOUTH);
        add(addPanel);

        add(Box.createVerticalStrut(20)); 

        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Jargon Term"));
        deletePanel.add(new JLabel("Term to Delete:"));
        deleteTermField = new JTextField(20);
        deletePanel.add(deleteTermField);
        JButton deleteButton = new JButton("Delete");
        deletePanel.add(deleteButton);
        
        deletePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, deletePanel.getPreferredSize().height));
        
        add(deletePanel);

        saveButton.addActionListener(e -> saveJargon());
        deleteButton.addActionListener(e -> deleteJargon());
    }

    private void saveJargon() {
        String term = termField.getText();
        String def = definitionArea.getText();
        if (term.isBlank() || def.isBlank()) {
            JOptionPane.showMessageDialog(this, "Term and Definition are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (dbService.addJargon(term, def)) {
            JOptionPane.showMessageDialog(this, "Jargon term saved!");
            termField.setText("");
            definitionArea.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save term.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteJargon() {
        String term = deleteTermField.getText();
        if (term.isBlank()) {
            JOptionPane.showMessageDialog(this, "Term to delete is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete the term '" + term + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            if (dbService.deleteJargon(term)) {
                JOptionPane.showMessageDialog(this, "Term deleted!");
                deleteTermField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete term (not found?).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}