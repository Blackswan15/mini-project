package com.legalsahayak.app.ui.panels;

import com.legalsahayak.app.model.UserSession;
import com.legalsahayak.app.service.AuthService;
import com.legalsahayak.app.ui.BasePanel; 
import com.legalsahayak.app.ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends BasePanel { 
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel statusLabel;
    private final AuthService authService; 

    public LoginPanel(MainFrame mainFrame, AuthService authService) {
        
        super(mainFrame, null); 
        this.authService = authService; 

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel("Welcome to Legal Sahayak");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, gbc);

        // Username
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset to one column
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(20);
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        statusLabel = new JLabel(" "); 
        statusLabel.setForeground(Color.RED);
        add(statusLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridy = 4;
        add(buttonPanel, gbc);

        loginButton.addActionListener(e -> handleLogin());
        
        passwordField.addActionListener(e -> handleLogin()); 
        
        registerButton.addActionListener(e -> handleRegister());
    }


    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isBlank() || password.isBlank()) {
            statusLabel.setText("Username and password are required.");
            return;
        }

        try {
            UserSession session = authService.login(username, password);
            
            mainFrame.setSession(session); 
            mainFrame.switchPanel("MainMenu"); 
            clearFields(); 
            
        } catch (AuthService.AuthException ex) {
            statusLabel.setText(ex.getMessage());
        }
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isBlank() || password.isBlank()) {
            statusLabel.setText("Username and password are required.");
            return;
        }

        try {
            authService.register(username, password);
            statusLabel.setForeground(Color.GREEN.darker());
            statusLabel.setText("Registration successful! Please login.");
            clearFields();
            
        } catch (AuthService.AuthException ex) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText(ex.getMessage());
        }
    }
    

    private void clearFields() {
        passwordField.setText("");
        statusLabel.setText(" ");
    }
}