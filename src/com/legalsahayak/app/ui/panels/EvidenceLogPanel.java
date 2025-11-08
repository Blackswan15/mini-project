package com.legalsahayak.app.ui.panels;

import com.legalsahayak.app.db.IDatabaseService;
import com.legalsahayak.app.model.LogEntry;
import com.legalsahayak.app.ui.BasePanel;
import com.legalsahayak.app.ui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EvidenceLogPanel extends BasePanel {

    private final JTable logTable;
    private final DefaultTableModel tableModel;
    private final JComboBox<String> categoryFilter;

    public EvidenceLogPanel(MainFrame mainFrame, IDatabaseService dbService) {
        super(mainFrame, dbService); 
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Evidence Log", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> mainFrame.switchPanel("MainMenu"));
        topPanel.add(backButton, BorderLayout.WEST);
        
        JButton helpButton = new JButton("Need Help Now?");
        helpButton.setForeground(Color.RED.darker());
        helpButton.setFont(new Font("Arial", Font.BOLD, 12));
        helpButton.addActionListener(e -> showSupportDialog()); 
        topPanel.add(helpButton, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Timestamp", "Category", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        logTable = new JTable(tableModel);
        logTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(logTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Category:"));
        String[] categories = {"All", "Harassment", "Incident", "Other"}; 
        categoryFilter = new JComboBox<>(categories);
        filterPanel.add(categoryFilter);
        
        JButton refreshButton = new JButton("Refresh");
        filterPanel.add(refreshButton);
        bottomPanel.add(filterPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add New Entry (Create)");
        JButton updateButton = new JButton("Update Selected (Update)");
        JButton deleteButton = new JButton("Delete Selected (Delete)");
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(bottomPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> refreshTable());
        categoryFilter.addActionListener(e -> refreshTable());
        addButton.addActionListener(e -> addEntry());
        updateButton.addActionListener(e -> updateEntry());
        deleteButton.addActionListener(e -> deleteEntry());
        
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent e) {
                refreshTable();
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent e) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent e) {}
        });
    }

    private void showSupportDialog() {
        String title = "Legal & Emotional Support Resources (India)";
        
        String message = """
            We understand that logging events can be difficult. 
            Please remember, this log is private to your computer.
            
            If you are in distress, please reach out to a professional.
            
            **Emotional Support:**
            - Kiran (Mental Health Rehab): 99221 84401
            - Vandrevala Foundation: 91235 65402
            - iCALL (TISS): 86372 12836 (Mon-Sat, 10am-8pm)
            
            **Legal & Domestic Support:**
            - National Commission for Women (NCW): 7827170170 (WhatsApp)
            - NALSA (Free Legal Aid): 15100
            - Women Helpline (All States): 1091
            
            You are not alone.
            """;
        
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBackground(getBackground());
        textArea.setMargin(new Insets(5, 5, 5, 5));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300)); 
        
        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }
    private void refreshTable() {
        if (getUsername() == null) return;
        tableModel.setRowCount(0);

        String selectedCategory = (String) categoryFilter.getSelectedItem();
        List<LogEntry> entries;

        if (selectedCategory == null || "All".equals(selectedCategory)) {
            entries = dbService.getLogEntries(getUsername()); 
        } else {
            entries = dbService.getLogEntries(getUsername(), selectedCategory); 
        }

        for (LogEntry entry : entries) {
            tableModel.addRow(new Object[]{
                entry.getId(),
                entry.getTimestamp(),
                entry.getCategory(),
                entry.getDescription()
            });
        }
    }
    
    private void addEntry() {
        String category = JOptionPane.showInputDialog(this, "Enter Category:");
        if (category == null || category.isBlank()) return;
        
        String description = JOptionPane.showInputDialog(this, "Enter Description:");
        if (description == null || description.isBlank()) return;
        
        dbService.addLogEntry(getUsername(), description, category);
        refreshTable();
    }
    
    private void updateEntry() {
        int selectedRow = logTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an entry to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int logId = (int) tableModel.getValueAt(selectedRow, 0);
        String oldDesc = (String) tableModel.getValueAt(selectedRow, 3);
        
        String newDesc = JOptionPane.showInputDialog(this, "Enter new description:", oldDesc);
        if (newDesc == null) return;
        
        if (dbService.updateLogEntry(logId, newDesc, getUsername())) {
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Error: Could not update entry.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteEntry() {
        int selectedRow = logTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an entry to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int logId = (int) tableModel.getValueAt(selectedRow, 0);
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this entry?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            if (dbService.deleteLogEntry(logId, getUsername())) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Could not delete entry.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}