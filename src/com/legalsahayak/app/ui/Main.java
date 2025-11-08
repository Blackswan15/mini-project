package com.legalsahayak.app.ui;

import com.legalsahayak.app.db.IDatabaseService;
import com.legalsahayak.app.db.SqliteDatabaseService;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                IDatabaseService dbService = new SqliteDatabaseService();
                dbService.connect(); 
                MainFrame mainFrame = new MainFrame(dbService);
                mainFrame.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Could not connect to the database. The application will exit.\n" + e.getMessage(), 
                    "Fatal Database Error", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}