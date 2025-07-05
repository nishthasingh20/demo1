package com.example.demo1;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import org.springframework.messaging.handler.annotation.SendTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class GreetingController {
    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) {
        logger.info("Received message from: {}", message.getName());
        try {
            Thread.sleep(1000); // simulated delay -> 1s
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String response = "Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!";
        logger.info("Sending response: {}", response);
        return new Greeting(response);
    }
}
