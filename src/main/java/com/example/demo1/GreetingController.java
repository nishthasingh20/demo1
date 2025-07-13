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

@Controller
public class GreetingController {
    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessage greeting(@DestinationVariable String roomId, ChatMessage message) {
        logger.info("Received message in room '{}': {} - {}", roomId, message.getSender(), message.getContent());
//        try {
//            Thread.sleep(1000); // simulated delay -> 1s
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//        String response = "Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!";
//        logger.info("Sending response: {}", response);
//        return new Greeting(response);
        return message;
    }

    @MessageMapping("/chatroom/{email}")
    public void privateChat(@DestinationVariable String email, ChatMessage message) {
        logger.info("Received private message for '{}': {} - {}", email, message.getSender(), message.getContent());

        // Send message to recipient
        messagingTemplate.convertAndSend("/topic/chatroom/" + email, message);

        // Send message to sender as well (if different from recipient)
        String sender = message.getSender();
        if (!sender.equals(email)) {
            logger.info("Also sending message to sender: {}", sender);
            messagingTemplate.convertAndSend("/topic/chatroom/" + sender, message);
        }
    }
}
