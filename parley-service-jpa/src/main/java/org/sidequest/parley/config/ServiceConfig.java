package org.sidequest.parley.config;

import org.sidequest.parley.repository.ChatMessageRepository;
import org.sidequest.parley.repository.ChatRoomRepository;
import org.sidequest.parley.repository.EnrollmentRepository;
import org.sidequest.parley.repository.UserRepository;
import org.sidequest.parley.service.ChatMessageService;
import org.sidequest.parley.service.ChatRoomService;
import org.sidequest.parley.service.EnrollmentService;
import org.sidequest.parley.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public ChatMessageService chatMessageService(ChatMessageRepository chatMessageRepository, UserService userService, ChatRoomService chatRoomService) {
        ChatMessageService service = new ChatMessageService();
        service.setChatMessageRepository(chatMessageRepository);
        service.setUserService(userService);
        service.setChatRoomService(chatRoomService);
        return service;
    }

    @Bean
    public ChatRoomService chatRoomService(ChatRoomRepository chatRoomRepository, EnrollmentService enrollmentService) {
        ChatRoomService service = new ChatRoomService();
        service.setChatRoomRepository(chatRoomRepository);
        service.setEnrollmentService(enrollmentService);
        return service;
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        UserService service = new UserService();
        service.setUserRepository(userRepository);
        return service;
    }

    @Bean
    public EnrollmentService enrollmentService(EnrollmentRepository enrollmentRepository) {
        EnrollmentService service = new EnrollmentService();
        service.setEnrollmentRepository(enrollmentRepository);
        return service;
    }
}