package com.legalsahayak.app.ui.panels;

import com.legalsahayak.app.db.IDatabaseService;
import com.legalsahayak.app.model.LawyerProfile;
import com.legalsahayak.app.ui.BasePanel;
import com.legalsahayak.app.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class RightsNavigatorPanel extends BasePanel {

    private final JList<String> categoryList;
    private final DefaultListModel<String> listModel;
    private final Map<String, Integer> categoryMap; 
    private final JTextArea summaryArea;
    private final JTextArea rightsArea;
    private final JTextArea lawyersArea;

    public RightsNavigatorPanel(MainFrame mainFrame, IDatabaseService dbService) {
        super(mainFrame, dbService); 
        setLayout(new BorderLayout(10, 10)); 
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 10px padding

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Rights Navigator", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> mainFrame.switchPanel("MainMenu"));
        topPanel.add(backButton, BorderLayout.WEST);
        
        add(topPanel, BorderLayout.NORTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        listModel = new DefaultListModel<>();
        categoryList = new JList<>(listModel);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        categoryMap = new java.util.LinkedHashMap<>();
        Map<Integer, String> dbCategories = dbService.getLegalCategories();
        for (Map.Entry<Integer, String> entry : dbCategories.entrySet()) {
            listModel.addElement(entry.getValue());
            categoryMap.put(entry.getValue(), entry.getKey()); 
        }
        
        JScrollPane listScrollPane = new JScrollPane(categoryList);
        listScrollPane.setMinimumSize(new Dimension(200, 100)); 
        splitPane.setLeftComponent(listScrollPane);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        summaryArea = createReadOnlyTextArea(); 
        rightsArea = createReadOnlyTextArea();
        lawyersArea = createReadOnlyTextArea();
        
        tabbedPane.addTab("Summary", new JScrollPane(summaryArea));
        tabbedPane.addTab("Key Rights", new JScrollPane(rightsArea));
        tabbedPane.addTab("Find Lawyers", new JScrollPane(lawyersArea));
        
        splitPane.setRightComponent(tabbedPane);
        
        add(splitPane, BorderLayout.CENTER);
        
        categoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { 
                loadCategoryInfo(categoryList.getSelectedValue());
            }
        });
        
        if (!listModel.isEmpty()) {
            categoryList.setSelectedIndex(0);
        }
    }
    private JTextArea createReadOnlyTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setMargin(new Insets(10, 10, 10, 10)); // Padding
        return textArea;
    }
    private void loadCategoryInfo(String categoryName) {
        if (categoryName == null) return;
        int categoryId = categoryMap.get(categoryName);
        
        String summary = dbService.getLegalInfo(categoryId, "summary");
        summaryArea.setText(summary);
        summaryArea.setCaretPosition(0); // Scroll to top
        
        String rights = dbService.getLegalInfo(categoryId, "key_rights");
        rightsArea.setText(rights);
        rightsArea.setCaretPosition(0);
        
        List<LawyerProfile> lawyers = dbService.getLawyers(categoryId);
        if (lawyers.isEmpty()) {
            lawyersArea.setText("No lawyers found for this category.");
        } else {
            StringBuilder lawyerText = new StringBuilder();
            for (LawyerProfile lawyer : lawyers) {
                lawyerText.append(lawyer.toString());
                lawyerText.append("\n---------------------\n");
            }
            lawyersArea.setText(lawyerText.toString());
            lawyersArea.setCaretPosition(0);
        }
    }
}