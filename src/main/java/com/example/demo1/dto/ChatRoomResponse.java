package com.example.demo1.dto;

/**
 * DTO for chat room creation response
 */
public class ChatRoomResponse {
    private String roomId;

    // Default constructor
    public ChatRoomResponse() {
    }

    // Constructor with parameters
    public ChatRoomResponse(String roomId) {
        this.roomId = roomId;
    }

    // Getters and setters
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}