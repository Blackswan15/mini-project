package com.legalsahayak.app.db;

import com.legalsahayak.app.model.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDatabaseService {

    void connect() throws SQLException;

    boolean doesUserExist(String username);
    Map<String, String> getLoginDetails(String username);
    boolean registerUser(String username, String hashedPassword);

    Map<Integer, String> getMainMenu();
    int getExitOptionId();

    Map<Integer, String> getLegalCategories();
    String getLegalInfo(int categoryId, String infoType);
    List<LawyerProfile> getLawyers(int categoryId);

    boolean addLawyer(String name, String spec, String exp, String ph, String mail, int catId);
    boolean updateLawyer(int lawyerId, String field, String value);
    boolean deleteLawyer(int lawyerId);
    boolean updateLegalInfo(int catId, String type, String content);
    boolean addJargon(String term, String definition);
    boolean deleteJargon(String term);

    List<QuizCard> getQuizQuestions(int limit);

    String getJargonDefinition(String term);

    void addLogEntry(String user, String desc, String cat);
    
    List<LogEntry> getLogEntries(String username); 
    List<LogEntry> getLogEntries(String username, String category); 
    
    boolean updateLogEntry(int logId, String newDesc, String user);
    boolean deleteLogEntry(int logId, String user);

    void saveRTIRequest(String user, String auth, String info, String name, String addr, String genText);
    List<Map<String, String>> getUserRTIRequests(String username);
    boolean updateRTIStatus(int rtiId, String newStatus, String user);
    boolean deleteRTIRequest(int rtiId, String user);
}