package com.example.demo1.service;

import com.example.demo1.entity.ChatMessage;
import com.example.demo1.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {
    private static final Logger logger = LoggerFactory.getLogger(ChatMessageService.class);

    @Autowired
    private ChatMessageRepository repo;

    public ChatMessage save(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        return repo.save(message);
    }

    public List<ChatMessage> getMessages(String roomId) {
        logger.info("Service: Fetching messages for roomId: {}", roomId);
        List<ChatMessage> messages = repo.findByRoomIdOrderByTimestampAsc(roomId);
        logger.info("Service: Found {} messages for roomId: {}", messages.size(), roomId);

        // If no messages found, let's check if there are any messages in the database at all
        if (messages.isEmpty()) {
            long totalCount = repo.count();
            logger.info("Service: Total messages in database: {}", totalCount);

            if (totalCount > 0) {
                // Log a few sample roomIds from the database to help debug
                List<ChatMessage> sampleMessages = repo.findAll().subList(0, Math.min(5, (int)totalCount));
                for (ChatMessage msg : sampleMessages) {
                    logger.info("Service: Sample message - roomId: {}, sender: {}, content: {}", 
                                msg.getRoomId(), msg.getSender(), msg.getContent());
                }
            }
        }

        return messages;
    }

    public List<ChatMessage> getAllMessages() {
        logger.info("Service: Fetching all messages");
        List<ChatMessage> allMessages = repo.findAll();
        logger.info("Service: Found {} total messages", allMessages.size());
        return allMessages;
    }

    public List<ChatMessage> getMessagesBySender(String sender) {
        logger.info("Service: Fetching messages for sender: {}", sender);
        List<ChatMessage> messages = repo.findBySenderOrderByTimestampAsc(sender);
        logger.info("Service: Found {} messages for sender: {}", messages.size(), sender);
        return messages;
    }

    public List<ChatMessage> getMessagesByPartialRoomId(String partialRoomId) {
        logger.info("Service: Fetching messages for partial roomId: {}", partialRoomId);
        List<ChatMessage> messages = repo.findByRoomIdContainingOrderByTimestampAsc(partialRoomId);
        logger.info("Service: Found {} messages for partial roomId: {}", messages.size(), partialRoomId);
        return messages;
    }
}
