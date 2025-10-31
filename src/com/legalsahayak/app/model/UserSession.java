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
    
    /**
     * A helper method to easily check if the user is an admin.
     * @return true if the user's role is 'admin', false otherwise.
     */
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }
}