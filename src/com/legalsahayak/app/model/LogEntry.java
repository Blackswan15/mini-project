package com.legalsahayak.app.model;

public class LogEntry {
    private final int id; // Added for CRUD
    private final String timestamp;
    private final String description;
    private final String category;

    public LogEntry(int id, String timestamp, String desc, String category) {
        this.id = id;
        this.timestamp = timestamp;
        this.description = desc;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format(
            "  [ID: %d] (%s) - [%s]\n  %s",
            id, timestamp, category, description
        );
    }
}