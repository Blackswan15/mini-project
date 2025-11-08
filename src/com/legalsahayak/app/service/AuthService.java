package com.legalsahayak.app.service;

import com.legalsahayak.app.db.IDatabaseService;
import com.legalsahayak.app.model.UserSession;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private final IDatabaseService dbService;
    public AuthService(IDatabaseService dbService) {
        this.dbService = dbService;
    }

    public UserSession login(String username, String password) throws AuthException {
        Map<String, String> details = dbService.getLoginDetails(username);

        if (details == null) {
            throw new AuthException("Error: User not found.");
        }

        if (BCrypt.checkpw(password, details.get("hash"))) {
            return new UserSession(username, details.get("role"));
        } else {
            throw new AuthException("Error: Invalid password.");
        }
    }
    
 
    public void register(String username, String password) throws AuthException {
        if (dbService.doesUserExist(username)) {
            throw new AuthException("Error: Username already exists.");
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        
        if (!dbService.registerUser(username, hashedPassword)) {
            throw new AuthException("Error: Registration failed due to a database error.");
        }
    }

    public static class AuthException extends Exception {
        public AuthException(String message) {
            super(message);
        }
    }
}