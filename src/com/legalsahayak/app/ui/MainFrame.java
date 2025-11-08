package com.legalsahayak.app.ui;

import com.legalsahayak.app.db.IDatabaseService;
import com.legalsahayak.app.model.UserSession;
import com.legalsahayak.app.service.AuthService;

import com.legalsahayak.app.ui.panels.*;
import com.legalsahayak.app.ui.panels.admin.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final IDatabaseService dbService;
    private final AuthService authService;
    
    private UserSession session; 
    public MainFrame(IDatabaseService dbService) {
        this.dbService = dbService;
        this.authService = new AuthService(dbService);

        setTitle("Legal Sahayak");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        BasePanel loginPanel = new LoginPanel(this, authService);
        BasePanel mainMenuPanel = new MainMenuPanel(this, dbService);
        BasePanel rightsPanel = new RightsNavigatorPanel(this, dbService);
        BasePanel evidencePanel = new EvidenceLogPanel(this, dbService);
        BasePanel quizPanel = new QuizPanel(this, dbService);
        BasePanel rtiPanel = new RtiPanel(this, dbService);
        BasePanel jargonPanel = new JargonPanel(this, dbService);
        BasePanel adminPanel = new AdminPanel(this, dbService);
        
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(mainMenuPanel, "MainMenu");
        mainPanel.add(rightsPanel, "RightsNavigator");
        mainPanel.add(evidencePanel, "EvidenceLog");
        mainPanel.add(quizPanel, "Quiz");
        mainPanel.add(rtiPanel, "RTI");
        mainPanel.add(jargonPanel, "Jargon");
        mainPanel.add(adminPanel, "Admin");

        add(mainPanel);
        
        cardLayout.show(mainPanel, "Login");
    }
    public void switchPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
    
    public void setSession(UserSession session) {
        this.session = session;
        if (session == null) {
            setTitle("Legal Sahayak");
            switchPanel("Login"); 
        } else {
            setTitle("Legal Sahayak - Welcome " + session.getUsername());
        }
    }

    public UserSession getSession() {
        return session;
    }
    
    public IDatabaseService getDbService() {
        return dbService;
    }
}