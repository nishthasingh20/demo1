package com.example.demo1.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String sender;
    private String content;
    private String roomId;

    private LocalDateTime timestamp;

    // Constructors
    public ChatMessage() {}

    public ChatMessage(String sender, String content, String roomId, LocalDateTime timestamp) {
        this.sender = sender;
        this.content = content;
        this.roomId = roomId;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
