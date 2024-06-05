package com.example.pcm.model;

public class UserSession {
    private static UserSession instance;
    private String username;
    private boolean loggedIn;

    private UserSession() {
        this.loggedIn = false;
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void login(String username) {
        this.username = username;
        this.loggedIn = true;
    }

    public void logout() {
        this.username = null;
        this.loggedIn = false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getUsername() {
        return username;
    }
}
