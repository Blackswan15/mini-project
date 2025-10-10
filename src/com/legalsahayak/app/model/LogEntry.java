package com.legalsahayak.app.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogEntry {
    private final LocalDateTime timestamp;
    private final String description;
    private final String category;

    public LogEntry(String description, String category) {
        this.timestamp = LocalDateTime.now();
        this.description = description;
        this.category = category;
    }

    public String getCategory() { // <-- ADD THIS METHOD
        return category;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "----------------------------------------\n"
               + "[" + timestamp.format(formatter) + "] [" + category + "]\n"
               + "  " + description;
    }
}