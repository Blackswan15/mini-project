
package com.legalsahayak.app.ui.panels.admin; 

import com.legalsahayak.app.db.IDatabaseService;
import com.legalsahayak.app.model.LawyerProfile;
import com.legalsahayak.app.ui.BasePanel;
import com.legalsahayak.app.ui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
public class AdminLawyerPanel extends BasePanel {

    private final JComboBox<String> categoryFilter;
    private final JTable lawyerTable;
    private final DefaultTableModel tableModel;
    private final Map<String, Integer> categoryMap;

    public AdminLawyerPanel(MainFrame mainFrame, IDatabaseService dbService) {
        super(mainFrame, dbService);
        setLayout(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Filter by Category:"));
        
        categoryMap = new java.util.LinkedHashMap<>();
        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("All"); 
        Map<Integer, String> dbCategories = dbService.getLegalCategories();
        for (Map.Entry<Integer, String> entry : dbCategories.entrySet()) {
            categoryFilter.addItem(entry.getValue());
            categoryMap.put(entry.getValue(), entry.getKey()); // Store Name -> ID
        }
        topPanel.add(categoryFilter);
        
        JButton refreshButton = new JButton("Refresh");
        topPanel.add(refreshButton);
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Name", "Specialization", "Experience", "Phone", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        lawyerTable = new JTable(tableModel);
        lawyerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(lawyerTable), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Lawyer");
        JButton updateButton = new JButton("Update Lawyer");
        JButton deleteButton = new JButton("Delete Lawyer");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);


        refreshButton.addActionListener(e -> refreshTable());
        categoryFilter.addActionListener(e -> refreshTable());
        addButton.addActionListener(e -> addLawyer());
        updateButton.addActionListener(e -> updateLawyer());
        deleteButton.addActionListener(e -> deleteLawyer());
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        
        List<LawyerProfile> lawyers = new java.util.ArrayList<>();
        if ("All".equals(selectedCategory)) {
            for (int catId : categoryMap.values()) {
                lawyers.addAll(dbService.getLawyers(catId));
            }
        } else {
            int catId = categoryMap.get(selectedCategory);
            lawyers = dbService.getLawyers(catId);
        }
        
        for (LawyerProfile lawyer : lawyers) {
            tableModel.addRow(new Object[]{
                lawyer.getId(),
                lawyer.toString().split("\n")[1].replace("  Name: ", ""), // A bit hacky
                lawyer.toString().split("\n")[2].replace("  Specialization: ", ""),
                lawyer.toString().split("\n")[3].replace("  Experience Note: ", ""),
                lawyer.toString().split("\n")[4].replace("  Phone Number: ", ""),
                lawyer.toString().split("\n")[5].replace("  Email: ", "")
            });
        }
    }
    

    private void addLawyer() {
        JTextField nameField = new JTextField();
        JTextField specField = new JTextField();
        JTextField expField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JComboBox<String> catCombo = new JComboBox<>(categoryMap.keySet().toArray(new String[0]));
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5)); // 0 rows, 2 cols
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Specialization:")); panel.add(specField);
        panel.add(new JLabel("Experience:")); panel.add(expField);
        panel.add(new JLabel("Phone:")); panel.add(phoneField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Category:")); panel.add(catCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Lawyer", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
        if (result == JOptionPane.OK_OPTION) {
            int catId = categoryMap.get((String)catCombo.getSelectedItem());
            boolean success = dbService.addLawyer(
                nameField.getText(), specField.getText(), expField.getText(),
                phoneField.getText(), emailField.getText(), catId
            );
            if (success) refreshTable();
            else JOptionPane.showMessageDialog(this, "Failed to add lawyer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateLawyer() {
        int selectedRow = lawyerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lawyer to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int lawyerId = (int) tableModel.getValueAt(selectedRow, 0);
        String field = JOptionPane.showInputDialog(this, "Enter field to update (name, specialization, experience, phone, email):");
        if (field == null || field.isBlank()) return;
        
        String value = JOptionPane.showInputDialog(this, "Enter new value for " + field + ":");
        if (value == null) return; // Allow blank value
        
        if (dbService.updateLawyer(lawyerId, field.toLowerCase(), value)) {
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update. Check if field name is valid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteLawyer() {
        int selectedRow = lawyerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lawyer to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int lawyerId = (int) tableModel.getValueAt(selectedRow, 0);
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete lawyer ID " + lawyerId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            if (dbService.deleteLawyer(lawyerId)) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete lawyer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}