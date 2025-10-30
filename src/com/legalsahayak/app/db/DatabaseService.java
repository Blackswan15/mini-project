package com.legalsahayak.app.db;

import com.legalsahayak.app.model.LawyerProfile;
import com.legalsahayak.app.model.LogEntry;
import com.legalsahayak.app.model.QuizCard;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles all RUNTIME database operations.
 * Connects to the database and provides query methods for services.
 */
public class DatabaseService {

    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:legalsahayak.db";

    /**
     * Connects to the SQLite database and enables foreign keys.
     */
    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
            System.out.println("Database connection established.");
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            System.exit(1);
        }
    }

    // --- Methods for AuthService ---

    public boolean doesUserExist(String username) {
        String sql = "SELECT 1 FROM Users WHERE username = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, username);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next(); // True if user exists
            }
        } catch (SQLException e) {
            System.err.println("Error checking user: " + e.getMessage());
            return true;
        }
    }

    public String getUserPasswordHash(String username) {
        String sql = "SELECT password_hash FROM Users WHERE username = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, username);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password_hash");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user hash: " + e.getMessage());
        }
        return null; // User not found
    }

    public boolean registerUser(String username, String hashedPassword) {
        String sql = "INSERT INTO Users(username, password_hash) VALUES(?, ?)";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, username);
            p.setString(2, hashedPassword);
            p.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
        
    }

    // --- Methods for UIHandler ---

    public Map<Integer, String> getMainMenu() {
        Map<Integer, String> menu = new LinkedHashMap<>();
        String sql = "SELECT id, option_text FROM MainMenu ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                menu.put(rs.getInt("id"), rs.getString("option_text"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching main menu: " + e.getMessage());
        }
        return menu;
    }
    
    public int getExitOptionId() {
        String sql = "SELECT id FROM MainMenu WHERE option_text = 'Exit'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching exit option: " + e.getMessage());
        }
        return 7; // Default fallback
    }

    // --- Methods for RightsNavigatorService ---

    public Map<Integer, String> getLegalCategories() {
        Map<Integer, String> categories = new LinkedHashMap<>();
        String sql = "SELECT id, category_name FROM LegalCategories ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.put(rs.getInt("id"), rs.getString("category_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching categories: " + e.getMessage());
        }
        return categories;
    }

    public String getLegalInfo(int categoryId, String infoType) {
        String sql = "SELECT content FROM LegalInfo WHERE category_id = ? AND info_type = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setInt(1, categoryId);
            p.setString(2, infoType);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("content");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching legal info: " + e.getMessage());
        }
        return "Content not found.";
    }

    public List<LawyerProfile> getLawyers(int categoryId) {
        List<LawyerProfile> lawyers = new ArrayList<>();
        String sql = "SELECT * FROM Lawyers WHERE category_id = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setInt(1, categoryId);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    lawyers.add(new LawyerProfile(
                        rs.getString("name"), rs.getString("specialization"),
                        rs.getString("experience"), rs.getString("phone"),
                        rs.getString("email")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching lawyers: " + e.getMessage());
        }
        return lawyers;
    }

    // --- Methods for QuizService ---

    public List<QuizCard> getQuizQuestions(int limit) {
        List<QuizCard> questions = new ArrayList<>();
        String sql = """
        SELECT q.question, q.options, q.correct_option_index, 
               q.explanation, c.category_name 
        FROM QuizQuestions q
        JOIN LegalCategories c ON q.category_id = c.id
        ORDER BY RANDOM() LIMIT ?
        """;
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setInt(1, limit);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    String[] options = rs.getString("options").split("\\|");
                    questions.add(new QuizCard(
                        rs.getString("question"), options,
                        rs.getInt("correct_option_index"),
                        rs.getString("explanation"), rs.getString("category_name")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching quiz questions: " + e.getMessage());
        }
        return questions;
    }

    // --- Methods for JargonBusterService ---

    public String getJargonDefinition(String term) {
        String sql = "SELECT definition FROM Jargon WHERE term = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, term.toUpperCase());
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("definition");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching jargon: " + e.getMessage());
        }
        return null;
    }

    // --- Methods for EvidenceLogService ---

    public void addLogEntry(String username, String description, String category) {
        String sql = "INSERT INTO EvidenceLog(username, timestamp, category, description) VALUES(?, ?, ?, ?)";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, username);
            p.setString(2, timestamp);
            p.setString(3, category);
            p.setString(4, description);
            p.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving log entry: " + e.getMessage());
        }
    }

    public List<LogEntry> getLogEntries(String username, String category) {
        List<LogEntry> entries = new ArrayList<>();
        String sql = "SELECT timestamp, category, description FROM EvidenceLog WHERE username = ?";
        
        if (category != null) {
            sql += " AND category = ?";
        }
        sql += " ORDER BY timestamp DESC";

        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, username);
            if (category != null) {
                p.setString(2, category);
            }
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    entries.add(new LogEntry(
                        rs.getString("timestamp"),
                        rs.getString("description"),
                        rs.getString("category")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching log entries: " + e.getMessage());
        }
        return entries;
    }

    // --- Methods for RtiService ---

    public void saveRTIRequest(String username, String authority, String info, 
                               String name, String addr, String generatedText) {
        String sql = """
        INSERT INTO RTIRequests
        (username, authority, info_needed, full_name, address, generated_text, status) 
        VALUES(?, ?, ?, ?, ?, ?, 'Generated')
        """;
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, username);
            p.setString(2, authority);
            p.setString(3, info);
            p.setString(4, name);
            p.setString(5, addr);
            p.setString(6, generatedText);
            p.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving RTI request: " + e.getMessage());
        }
    }

    public boolean displayUserRTIRequests(String username) {
        String sql = "SELECT id, authority, status FROM RTIRequests WHERE username = ?";
        boolean found = false;
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, username);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    if (!found) {
                        System.out.println("\n--- Your Saved RTI Requests ---");
                        found = true;
                    }
                    System.out.println(
                        "ID: " + rs.getInt("id") + 
                        " | To: " + rs.getString("authority") + 
                        " | Status: " + rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching RTI requests: " + e.getMessage());
        }
        if (!found) {
            System.out.println("You have no saved RTI requests.");
        }
        return found;
    }

    public void updateRTIStatus(int rtiId, String newStatus, String username) {
        String sql = "UPDATE RTIRequests SET status = ? WHERE id = ? AND username = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, newStatus);
            p.setInt(2, rtiId);
            p.setString(3, username);
            
            int rows = p.executeUpdate();
            if (rows > 0) {
                System.out.println("Status updated successfully.");
            } else {
                System.out.println("Error: Could not find an RTI with that ID belonging to you.");
            }
        } catch (SQLException e) {
            System.err.println("Error updating status: " + e.getMessage());
        }
    }
}