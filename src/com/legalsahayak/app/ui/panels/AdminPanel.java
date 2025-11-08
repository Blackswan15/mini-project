package com.legalsahayak.app.ui.panels;

import com.legalsahayak.app.db.IDatabaseService;
import com.legalsahayak.app.ui.BasePanel;
import com.legalsahayak.app.ui.MainFrame;

import com.legalsahayak.app.ui.panels.admin.AdminInfoPanel;
import com.legalsahayak.app.ui.panels.admin.AdminJargonPanel;
import com.legalsahayak.app.ui.panels.admin.AdminLawyerPanel;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends BasePanel {

    public AdminPanel(MainFrame mainFrame, IDatabaseService dbService) {
        super(mainFrame, dbService);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Admin Tools", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> mainFrame.switchPanel("MainMenu"));
        topPanel.add(backButton, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        JTabbedPane adminTabs = new JTabbedPane();

        adminTabs.addTab("Manage Lawyers", new AdminLawyerPanel(mainFrame, dbService));
        adminTabs.addTab("Manage Legal Info", new AdminInfoPanel(mainFrame, dbService));
        adminTabs.addTab("Manage Jargon", new AdminJargonPanel(mainFrame, dbService));
        
        add(adminTabs, BorderLayout.CENTER);
    }
}