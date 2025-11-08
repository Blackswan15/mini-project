package com.legalsahayak.app.ui;

import com.legalsahayak.app.db.IDatabaseService;
import javax.swing.JPanel;

public abstract class BasePanel extends JPanel {
    
    protected MainFrame mainFrame;
    protected IDatabaseService dbService;

    public BasePanel(MainFrame mainFrame, IDatabaseService dbService) {
        this.mainFrame = mainFrame;
        this.dbService = dbService;
    }
    protected String getUsername() {
        if (mainFrame.getSession() != null) {
            return mainFrame.getSession().getUsername();
        }
        return null;
    }

    protected boolean isAdmin() {
        if (mainFrame.getSession() != null) {
            return mainFrame.getSession().isAdmin();
        }
        return false;
    }

}