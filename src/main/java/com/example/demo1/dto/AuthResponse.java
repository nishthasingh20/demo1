package com.example.demo1.dto;

public class AuthResponse {
    private boolean success;
    private String message;
    private String token;
    private String loggedinUsername;

    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.loggedinUsername = loggedinUsername;
    }

    public AuthResponse(boolean success, String message, String token, String loggedinUsername) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.loggedinUsername = loggedinUsername;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getLoggedinUsername() { return loggedinUsername; }
    public void setLoggedinUsername(String loggedinUsername) {this.loggedinUsername = loggedinUsername;}
}
