package com.legalsahayak.app.ui.panels;

import com.legalsahayak.app.db.IDatabaseService;
import com.legalsahayak.app.ui.BasePanel;
import com.legalsahayak.app.ui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class RtiPanel extends BasePanel {

    private JTabbedPane tabbedPane;
    private JTextField nameField, addressField, authorityField;
    private JTextArea infoArea, generatedRtiArea;
    private JButton saveButton;
    private JTable rtiTable;
    private DefaultTableModel tableModel;

    public RtiPanel(MainFrame mainFrame, IDatabaseService dbService) {
        super(mainFrame, dbService);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("RTI Service", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> mainFrame.switchPanel("MainMenu"));
        topPanel.add(backButton, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Generate New RTI", createGeneratePanel());
        tabbedPane.addTab("My Saved RTIs", createViewPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) {
                refreshRtiTable();
            }
        });
    }

    private JPanel createGeneratePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Your Full Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; nameField = new JTextField(30); formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Your Full Address:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; addressField = new JTextField(30); formPanel.add(addressField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Public Authority (e.g., 'Municipal Corp'):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; authorityField = new JTextField(30); formPanel.add(authorityField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Information Needed:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; infoArea = new JTextArea(5, 30); formPanel.add(new JScrollPane(infoArea), gbc);
        
        JButton generateButton = new JButton("Generate Text");
        gbc.gridx = 1; gbc.gridy = 4; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(generateButton, gbc);
        
        panel.add(formPanel, BorderLayout.NORTH);
        
        generatedRtiArea = new JTextArea();
        generatedRtiArea.setEditable(false);
        generatedRtiArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panel.add(new JScrollPane(generatedRtiArea), BorderLayout.CENTER);

        saveButton = new JButton("Save This Request");
        saveButton.setEnabled(false); 
        panel.add(saveButton, BorderLayout.SOUTH);

        generateButton.addActionListener(e -> {
            String rtiText = generateRtiText();
            generatedRtiArea.setText(rtiText);
            saveButton.setEnabled(true);
        });
        
        saveButton.addActionListener(e -> {
            dbService.saveRTIRequest(getUsername(), authorityField.getText(), infoArea.getText(),
                nameField.getText(), addressField.getText(), generatedRtiArea.getText());
            JOptionPane.showMessageDialog(this, "RTI Request Saved!");
            clearForm();
            generatedRtiArea.setText("");
            saveButton.setEnabled(false);
        });
        
        return panel;
    }
    
    private void clearForm() {
        nameField.setText("");
        addressField.setText("");
        authorityField.setText("");
        infoArea.setText("");
    }

    private String generateRtiText() {
        return String.format(
            "To: The Public Information Officer\n%s\n\n" +
            "Subject: Request for Information under RTI Act 2005\n\n" +
            "Applicant: %s\nAddress: %s\n\n" +
            "Details of Information Required:\n%s\n\n" +
            "Please provide this information as per the RTI Act.",
            authorityField.getText(), nameField.getText(), addressField.getText(), infoArea.getText()
        );
    }

    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        String[] columnNames = {"ID", "Authority", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        rtiTable = new JTable(tableModel);
        rtiTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(rtiTable), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton updateStatusButton = new JButton("Update Status");
        JButton deleteButton = new JButton("Delete Request");
        
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        updateStatusButton.addActionListener(e -> updateRtiStatus());
        deleteButton.addActionListener(e -> deleteRti());

        return panel;
    }
    private void refreshRtiTable() {
        if (getUsername() == null) return;
        tableModel.setRowCount(0); // Clear table
        List<Map<String, String>> requests = dbService.getUserRTIRequests(getUsername());
        for (Map<String, String> req : requests) {
            tableModel.addRow(new Object[]{
                req.get("id"), req.get("authority"), req.get("status")
            });
        }
    }
    private int getSelectedRtiId() {
        int selectedRow = rtiTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an RTI request from the table.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        String idString = (String) tableModel.getValueAt(selectedRow, 0);
        return Integer.parseInt(idString);
    }
    private void updateRtiStatus() {
        int rtiId = getSelectedRtiId();
        if (rtiId == -1) return;
        
        String newStatus = JOptionPane.showInputDialog(this, "Enter new status (e.g., 'Filed', 'Replied'):");
        if (newStatus != null && !newStatus.isBlank()) {
            if (dbService.updateRTIStatus(rtiId, newStatus, getUsername())) {
                refreshRtiTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update status (is this your request?).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void deleteRti() {
        int rtiId = getSelectedRtiId();
        if (rtiId == -1) return;
        
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this RTI request?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            if (dbService.deleteRTIRequest(rtiId, getUsername())) {
                refreshRtiTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete request (is this your request?).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}