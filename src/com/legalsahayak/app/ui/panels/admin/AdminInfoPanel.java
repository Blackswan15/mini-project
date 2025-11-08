package com.legalsahayak.app.ui.panels.admin;

import com.legalsahayak.app.db.IDatabaseService;
import com.legalsahayak.app.ui.BasePanel;
import com.legalsahayak.app.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
public class AdminInfoPanel extends BasePanel {

    private final JComboBox<String> categoryCombo;
    private final JComboBox<String> infoTypeCombo;
    private final JTextArea contentArea;
    private final Map<String, Integer> categoryMap; // Name -> ID

    public AdminInfoPanel(MainFrame mainFrame, IDatabaseService dbService) {
        super(mainFrame, dbService);
        setLayout(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Category:"));
        
        categoryMap = new java.util.LinkedHashMap<>();
        categoryCombo = new JComboBox<>();
        Map<Integer, String> dbCategories = dbService.getLegalCategories();
        for (Map.Entry<Integer, String> entry : dbCategories.entrySet()) {
            categoryCombo.addItem(entry.getValue());
            categoryMap.put(entry.getValue(), entry.getKey()); // Store Name -> ID
        }
        topPanel.add(categoryCombo);
        
        topPanel.add(new JLabel("Info Type:"));
        infoTypeCombo = new JComboBox<>(new String[]{"summary", "key_rights"});
        topPanel.add(infoTypeCombo);
        
        JButton loadButton = new JButton("Load Content");
        topPanel.add(loadButton);
        add(topPanel, BorderLayout.NORTH);

        contentArea = new JTextArea(20, 50);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        add(new JScrollPane(contentArea), BorderLayout.CENTER);
        
        JButton saveButton = new JButton("Save Content");
        add(saveButton, BorderLayout.SOUTH);

        loadButton.addActionListener(e -> loadContent());
        saveButton.addActionListener(e -> saveContent());
    }

    private void loadContent() {
        String catName = (String) categoryCombo.getSelectedItem();
        String infoType = (String) infoTypeCombo.getSelectedItem();
        if (catName == null) return;
        
        int catId = categoryMap.get(catName);
        String content = dbService.getLegalInfo(catId, infoType);
        contentArea.setText(content);
        contentArea.setCaretPosition(0);
    }
    
    private void saveContent() {
        String catName = (String) categoryCombo.getSelectedItem();
        String infoType = (String) infoTypeCombo.getSelectedItem();
        if (catName == null) return;
        
        int catId = categoryMap.get(catName);
        String content = contentArea.getText();
        
        if (dbService.updateLegalInfo(catId, infoType, content)) {
            JOptionPane.showMessageDialog(this, "Content saved successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save content.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}