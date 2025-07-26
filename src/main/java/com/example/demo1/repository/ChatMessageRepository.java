package com.example.demo1.repository;

import com.example.demo1.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findByRoomIdOrderByTimestampAsc(String roomId);
    List<ChatMessage> findBySenderOrderByTimestampAsc(String sender);

    // Find messages where roomId contains the given string
    List<ChatMessage> findByRoomIdContainingOrderByTimestampAsc(String partialRoomId);
}
