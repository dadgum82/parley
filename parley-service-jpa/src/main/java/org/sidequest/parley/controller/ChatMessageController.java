package org.sidequest.parley.controller;

import org.sidequest.parley.api.ChatsApi;
import org.sidequest.parley.model.ChatMessage;
import org.sidequest.parley.model.NewChatMessage;
import org.sidequest.parley.repository.ChatMessageRepository;
import org.sidequest.parley.repository.ChatRoomRepository;
import org.sidequest.parley.repository.ChatRoomUsersRepository;
import org.sidequest.parley.repository.UserRepository;
import org.sidequest.parley.service.ChatMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatMessageController implements ChatsApi {
    private static final Logger log = LoggerFactory.getLogger(ChatMessageController.class);

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRoomUsersRepository chatRoomUsersRepository;

    @Autowired
    ChatMessageService chatMessageService;


    @Override
    public ResponseEntity<ChatMessage> getChatMessageById(Long id) {
        try {
            log.info("getChatMessageById: " + id);
            ChatMessage cm = chatMessageService.getChatMessage(id);
            return ResponseEntity.ok(cm);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<ChatMessage> createChatMessage(NewChatMessage newChatMessage) {
        try {
            chatMessageService.createChatMessage(newChatMessage);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}