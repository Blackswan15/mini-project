package com.legalsahayak.app.model;

public class UserSession {
    private final String username;
    private final String role;

    public UserSession(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
   
     public boolean isAdmin() {
        if (this.role == null) {
            return false;
        }
        return "admin".equalsIgnoreCase(this.role);
    }
}