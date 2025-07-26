package com.example.demo1.controller;

import com.example.demo1.dto.ChatRoomRequest;
import com.example.demo1.dto.ChatRoomResponse;
import com.example.demo1.entity.ChatMessage;
import com.example.demo1.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatMessageRestController {
    private static final Logger logger = LoggerFactory.getLogger(ChatMessageRestController.class);

    @Autowired
    private ChatMessageService messageService;

    /**
     * Generates a room ID by combining and sorting user IDs
     * This ensures the same room ID is used regardless of the order of users
     */
    private String generateRoomId(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return null;
        }

        // Sort the user IDs alphabetically to ensure consistency
        Collections.sort(userIds);
        logger.info(userIds.toString());

        // Join the sorted user IDs with underscore
        return String.join("_", userIds);
    }

    /**
     * Creates a chat room for the given users
     * @param request Contains a list of user IDs
     * @return Response with the generated room ID
     */
    @PostMapping("/chat/room")
    public ChatRoomResponse createChatRoom(@RequestBody ChatRoomRequest request) {
        logger.info("Creating chat room for users: {}", request.getUserIds());


        String roomId = generateRoomId(request.getUserIds());
        logger.info("Generated room ID: {}", roomId);

        return new ChatRoomResponse(roomId);
    }

    @GetMapping("/messages/{roomId}")
    public List<ChatMessage> getChatMessages(@PathVariable String roomId) {
        try {
            // Try to URL-decode the roomId in case it contains special characters
            String decodedRoomId = URLDecoder.decode(roomId, StandardCharsets.UTF_8.toString());

            logger.info("Original roomId: {}", roomId);
            logger.info("Decoded roomId: {}", decodedRoomId);

            // First try with the decoded roomId
            List<ChatMessage> messages = messageService.getMessages(decodedRoomId);
            logger.info("Found {} messages for decoded roomId: {}", messages.size(), decodedRoomId);

            // If no messages found with decoded roomId, try with the original roomId
            if (messages.isEmpty() && !roomId.equals(decodedRoomId)) {
                logger.info("No messages found with decoded roomId, trying original roomId");
                messages = messageService.getMessages(roomId);
                logger.info("Found {} messages for original roomId: {}", messages.size(), roomId);
            }

            // If still no messages found, try with partial roomId match
            if (messages.isEmpty()) {
                logger.info("No messages found with exact roomId, trying partial match");

                // Try with the decoded roomId first
                messages = messageService.getMessagesByPartialRoomId(decodedRoomId);
                logger.info("Found {} messages for partial decoded roomId: {}", messages.size(), decodedRoomId);

                // If still no messages, try with the original roomId
                if (messages.isEmpty() && !roomId.equals(decodedRoomId)) {
                    logger.info("No messages found with partial decoded roomId, trying partial original roomId");
                    messages = messageService.getMessagesByPartialRoomId(roomId);
                    logger.info("Found {} messages for partial original roomId: {}", messages.size(), roomId);
                }
            }

            return messages;
        } catch (UnsupportedEncodingException e) {
            logger.error("Error decoding roomId: {}", e.getMessage());
            // Fall back to using the original roomId
            List<ChatMessage> messages = messageService.getMessages(roomId);
            logger.info("Found {} messages for original roomId: {}", messages.size(), roomId);

            // If no messages found, try with partial roomId match
            if (messages.isEmpty()) {
                logger.info("No messages found with exact roomId, trying partial match");
                messages = messageService.getMessagesByPartialRoomId(roomId);
                logger.info("Found {} messages for partial roomId: {}", messages.size(), roomId);
            }

            return messages;
        }
    }

    @GetMapping("/messages/debug/all")
    public List<ChatMessage> getAllMessages() {
        logger.info("Debug: Fetching all messages");
        List<ChatMessage> allMessages = messageService.getAllMessages();
        logger.info("Debug: Found {} total messages", allMessages.size());
        return allMessages;
    }

    @GetMapping("/messages/debug/sender/{sender}")
    public List<ChatMessage> getMessagesBySender(@PathVariable String sender) {
        logger.info("Debug: Fetching messages for sender: {}", sender);
        List<ChatMessage> messages = messageService.getMessagesBySender(sender);
        logger.info("Debug: Found {} messages for sender: {}", messages.size(), sender);
        return messages;
    }

    @GetMapping("/messages/debug/partial-room/{partialRoomId}")
    public List<ChatMessage> getMessagesByPartialRoomId(@PathVariable String partialRoomId) {
        logger.info("Debug: Fetching messages for partial roomId: {}", partialRoomId);
        List<ChatMessage> messages = messageService.getMessagesByPartialRoomId(partialRoomId);
        logger.info("Debug: Found {} messages for partial roomId: {}", messages.size(), partialRoomId);
        return messages;
    }
}
