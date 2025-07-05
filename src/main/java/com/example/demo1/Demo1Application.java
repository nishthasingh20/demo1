package com.example.demo1;

import org.springframework.boot.SpringApplication;

// Removed @SpringBootApplication to avoid conflict with MessagingStompWebsocketApplication
public class Demo1Application {

	public static void main(String[] args) {
		// Updated to use MessagingStompWebsocketApplication as the Spring Boot
		// application
		SpringApplication.run(MessagingStompWebsocketApplication.class, args);
	}
}
