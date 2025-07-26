package com.example.demo1.dto;

import java.util.List;

/**
 * DTO for chat room creation request
 */
public class ChatRoomRequest {
    private List<String> userIds;

    // Default constructor
    public ChatRoomRequest() {
    }

    // Constructor with parameters
    public ChatRoomRequest(List<String> userIds) {
        this.userIds = userIds;
    }

    // Getters and setters
    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}