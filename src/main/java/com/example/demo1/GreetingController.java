package com.example.demo1;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.SendTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.example.demo1.entity.ChatMessage;
import com.example.demo1.service.ChatMessageService;

@Controller
public class GreetingController {
    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageService chatMessageService;

    /**
     * Generates a unique room ID for private chats by combining and sorting the user emails
     * This ensures the same room ID is used regardless of who initiates the chat
     */
    private String generatePrivateChatRoomId(String user1, String user2) {
        // Create a list of users and sort them alphabetically to ensure consistency
        java.util.List<String> userIds = new java.util.ArrayList<>();
        userIds.add(user1);
        userIds.add(user2);
        java.util.Collections.sort(userIds);

        // Join the sorted user IDs with underscore
        return String.join("_", userIds);
    }

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessage greeting(@DestinationVariable String roomId, ChatMessage message) {
        logger.info("Received message in room '{}': {} - {}", roomId, message.getSender(), message.getContent());
        message.setRoomId(roomId);
        return chatMessageService.save(message);
//        try {
//            Thread.sleep(1000); // simulated delay -> 1s
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//        String response = "Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!";
//        logger.info("Sending response: {}", response);
//        return new Greeting(response);
    }

    @MessageMapping("/chatroom/{email}")
    public void privateChat(@DestinationVariable String email, ChatMessage message) {
        logger.info("Received private message for '{}': {} - {}", email, message.getSender(), message.getContent());

        // Create a unique roomId for private chats using sender and recipient emails
        String sender = message.getSender();
        String roomId = generatePrivateChatRoomId(sender, email);
        message.setRoomId(roomId);

        // Save the private message to database
        chatMessageService.save(message);

        // Send message to recipient
        messagingTemplate.convertAndSend("/topic/chatroom/" + email, message);

        // Send message to sender as well (if different from recipient)
        if (!sender.equals(email)) {
            logger.info("Also sending message to sender: {}", sender);
            messagingTemplate.convertAndSend("/topic/chatroom/" + sender, message);
        }
    }
}
