package com.legalsahayak.app.db;

import com.legalsahayak.app.model.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqliteDatabaseService implements IDatabaseService {

    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:legalsahayak.db";

    @Override
    public void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
            System.out.println("Database connection established.");
        } catch (Exception e) {
            System.err.println("Error connecting: " + e.getMessage());
            throw new SQLException("Error connecting to database: " + e.getMessage());
        }
    }

    @Override
    public boolean doesUserExist(String username) {
        String sql = "SELECT 1 FROM Users WHERE username = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, username);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next(); 
            }
        } catch (SQLException e) {
            System.err.println("Error checking user: " + e.getMessage());
            return true;
        }
    }

    @Override
    public Map<String, String> getLoginDetails(String username) {
        String sql = "SELECT password_hash, role FROM Users WHERE username = ?";
        Map<String, String> details = new LinkedHashMap<>();
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, username);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    details.put("hash", rs.getString("password_hash"));
                    details.put("role", rs.getString("role"));
                    return details;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user details: " + e.getMessage());
        }
        return null; 
    }

    @Override
    public boolean registerUser(String username, String hashedPassword) {
        String sql = "INSERT INTO Users(username, password_hash, role) " +
                     "VALUES(?, ?, 'user')";
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


    @Override
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
    
    @Override
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
        return 7; // Default fallback is now 7
    }

    @Override
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

    @Override
    public String getLegalInfo(int categoryId, String infoType) {
        String sql = "SELECT content FROM LegalInfo " +
                     "WHERE category_id = ? AND info_type = ?";
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

    @Override
    public List<LawyerProfile> getLawyers(int categoryId) {
        List<LawyerProfile> lawyers = new ArrayList<>();
        String sql = "SELECT id, name, specialization, experience, " + 
                       "phone, email FROM Lawyers WHERE category_id = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setInt(1, categoryId);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    lawyers.add(new LawyerProfile(
                        rs.getInt("id"), rs.getString("name"),
                        rs.getString("specialization"),
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

    @Override
    public boolean addLawyer(String name, String spec, String exp, 
                             String ph, String mail, int catId) {
        String sql = "INSERT INTO Lawyers(name, specialization, " + 
                     "experience, phone, email, category_id) " + 
                     "VALUES(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, name); p.setString(2, spec);
            p.setString(3, exp); p.setString(4, ph);
            p.setString(5, mail); p.setInt(6, catId);
            p.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding lawyer: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateLawyer(int lawyerId, String field, String value) {
        if (!field.equals("phone") && !field.equals("email") && !field.equals("specialization") && !field.equals("experience") && !field.equals("name")) {
            System.err.println("Invalid update field.");
            return false;
        }
        String sql = "UPDATE Lawyers SET " + field + " = ? WHERE id = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, value);
            p.setInt(2, lawyerId);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating lawyer: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteLawyer(int lawyerId) {
        String sql = "DELETE FROM Lawyers WHERE id = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setInt(1, lawyerId);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting lawyer: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateLegalInfo(int catId, String type, String content) {
        String sql = "INSERT INTO LegalInfo (category_id, info_type, content) "
                   + "VALUES (?, ?, ?) "
                   + "ON CONFLICT(category_id, info_type) "
                   + "DO UPDATE SET content = excluded.content";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setInt(1, catId);
            p.setString(2, type);
            p.setString(3, content);
            p.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating legal info: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addJargon(String term, String definition) {
        String sql = "INSERT INTO Jargon (term, definition) VALUES (?, ?) "
                   + "ON CONFLICT(term) DO UPDATE SET definition = excluded.definition";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, term.toUpperCase());
            p.setString(2, definition);
            p.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding jargon: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteJargon(String term) {
        String sql = "DELETE FROM Jargon WHERE term = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, term.toUpperCase());
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting jargon: " + e.getMessage());
            return false;
        }
    }


    @Override
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
                        rs.getString("explanation"), 
                        rs.getString("category_name")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching quiz: " + e.getMessage());
        }
        return questions;
    }

    @Override
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

    @Override
    public void addLogEntry(String user, String desc, String cat) {
        String sql = "INSERT INTO EvidenceLog(username, timestamp, " +
                     "category, description) VALUES(?, ?, ?, ?)";
        String ts = LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, user); p.setString(2, ts);
            p.setString(3, cat); p.setString(4, desc);
            p.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving log: " + e.getMessage());
        }
    }
    
    @Override
    public List<LogEntry> getLogEntries(String username) {
        return getLogEntries(username, null); // Calls the other method
    }

    @Override
    public List<LogEntry> getLogEntries(String username, String category) {
        List<LogEntry> entries = new ArrayList<>();
        String sql = "SELECT id, timestamp, category, description " + 
                     "FROM EvidenceLog WHERE username = ?";
        
        if (category != null && !category.isBlank()) {
            sql += " AND category = ?";
        }
        sql += " ORDER BY timestamp DESC";

        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, username);
            if (category != null && !category.isBlank()) {
                p.setString(2, category);
            }
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    entries.add(new LogEntry(
                        rs.getInt("id"), rs.getString("timestamp"),
                        rs.getString("description"),
                        rs.getString("category")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching logs: " + e.getMessage());
        }
        return entries;
    }

    @Override
    public boolean updateLogEntry(int logId, String newDesc, String user) {
        String sql = "UPDATE EvidenceLog SET description = ? " + 
                     "WHERE id = ? AND username = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, newDesc);
            p.setInt(2, logId);
            p.setString(3, user);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating log: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteLogEntry(int logId, String user) {
        String sql = "DELETE FROM EvidenceLog WHERE id = ? AND username = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setInt(1, logId);
            p.setString(2, user);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting log: " + e.getMessage());
            return false;
        }
    }
    @Override
    public void saveRTIRequest(String user, String auth, String info, 
                               String name, String addr, String genText) {
        String sql = "INSERT INTO RTIRequests(username, authority, " +
                     "info_needed, full_name, address, generated_text, " +
                     "status) VALUES(?, ?, ?, ?, ?, ?, 'Generated')";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, user); p.setString(2, auth);
            p.setString(3, info); p.setString(4, name);
            p.setString(5, addr); p.setString(6, genText);
            p.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving RTI: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, String>> getUserRTIRequests(String username) {
        String sql = "SELECT id, authority, status FROM RTIRequests " +
                     "WHERE username = ?";
        List<Map<String, String>> requests = new ArrayList<>();
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, username);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> request = new LinkedHashMap<>();
                    request.put("id", String.valueOf(rs.getInt("id"))); // As string for table
                    request.put("authority", rs.getString("authority"));
                    request.put("status", rs.getString("status"));
                    requests.add(request);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching RTIs: " + e.getMessage());
        }
        return requests;
    }

    @Override
    public boolean updateRTIStatus(int rtiId, String newStatus, String user) {
        String sql = "UPDATE RTIRequests SET status = ? " +
                     "WHERE id = ? AND username = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, newStatus);
            p.setInt(2, rtiId);
            p.setString(3, user);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating status: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deleteRTIRequest(int rtiId, String user) {
        String sql = "DELETE FROM RTIRequests WHERE id = ? AND username = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setInt(1, rtiId);
            p.setString(2, user);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting RTI: " + e.getMessage());
            return false;
        }
    }
}