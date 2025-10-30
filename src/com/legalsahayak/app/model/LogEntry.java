package com.legalsahayak.app.model;

public class LogEntry {
    private final String timestamp;
    private final String description;
    private final String category;

    public LogEntry(String timestamp, String description, String category) {
        this.timestamp = timestamp;
        this.description = description;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "----------------------------------------\n"
               + "[" + timestamp + "] [" + category + "]\n"
               + "  " + description;
    }
}