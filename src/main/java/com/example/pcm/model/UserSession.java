package com.example.pcm.model;

public class UserSession {
    private static UserSession instance;
    private boolean loggedIn;
    private String username;
    private String role;

    private UserSession() {
        // private constructor to enforce singleton pattern
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void login(String username, String role) {
        this.loggedIn = true;
        this.username = username;
        this.role = role;
    }

    public void logout() {
        this.loggedIn = false;
        this.username = null;
        this.role = null;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
