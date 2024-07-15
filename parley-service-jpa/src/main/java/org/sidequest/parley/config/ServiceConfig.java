package org.sidequest.parley.config;

import org.sidequest.parley.service.ChatMessageService;
import org.sidequest.parley.service.ChatRoomService;
import org.sidequest.parley.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public ChatRoomService chatRoomService() {
        // Assuming ChatRoomService has no dependencies or can be constructed with default settings
        return new ChatRoomService();
    }

    @Bean
    public UserService userService() {
        // Assuming UserService has no dependencies or can be constructed with default settings
        return new UserService();
    }

    @Bean
    public ChatMessageService chatMessageService(ChatRoomService chatRoomService, UserService userService) {
        // Injecting ChatRoomService and UserService into ChatMessageService
        return new ChatMessageService(chatRoomService, userService);
    }
}