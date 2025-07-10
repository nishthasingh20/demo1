package com.example.demo1.dto;

public class UserDTO {
    private String fullName;
    private String email;

    public UserDTO(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    // Getters and setters
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
