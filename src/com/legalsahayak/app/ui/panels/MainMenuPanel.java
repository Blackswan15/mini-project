package com.legalsahayak.app.ui.panels;

import com.legalsahayak.app.db.IDatabaseService;
import com.legalsahayak.app.ui.BasePanel; 
import com.legalsahayak.app.ui.MainFrame;
import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends BasePanel { 

    public MainMenuPanel(MainFrame mainFrame, IDatabaseService dbService) {
        super(mainFrame, dbService); 

        setLayout(new BorderLayout());
        
        JLabel title = new JLabel("Main Menu", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel buttonGrid = new JPanel(new GridLayout(0, 2, 20, 20)); 
        buttonGrid.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); 

        buttonGrid.add(createMenuButton("1. Rights Navigator", "RightsNavigator"));
        buttonGrid.add(createMenuButton("2. Evidence Log", "EvidenceLog"));
        buttonGrid.add(createMenuButton("3. Legal Quiz", "Quiz"));
        buttonGrid.add(createMenuButton("4. RTI Generator", "RTI"));
        buttonGrid.add(createMenuButton("5. Jargon Buster", "Jargon"));
        
        JButton adminButton = createMenuButton("6. Admin Tools", "Admin");
        buttonGrid.add(adminButton);
        
        add(buttonGrid, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            mainFrame.setSession(null); 
        });
        southPanel.add(logoutButton);
        add(southPanel, BorderLayout.SOUTH);
        
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent e) {
                adminButton.setVisible(isAdmin()); 
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent e) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent e) {}
        });
    }


    private JButton createMenuButton(String text, String panelName) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        
        button.addActionListener(e -> {
            mainFrame.switchPanel(panelName);
        });
        return button;
    }
}