package org.sidequest.parley.controller;

import org.sidequest.parley.api.ChatsApi;
import org.sidequest.parley.model.ChatMessage;
import org.sidequest.parley.model.NewChatMessage;
import org.sidequest.parley.repository.ChatMessageRepository;
import org.sidequest.parley.repository.ChatRoomRepository;
import org.sidequest.parley.repository.ChatRoomUsersRepository;
import org.sidequest.parley.repository.UserRepository;
import org.sidequest.parley.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatMessageController implements ChatsApi {

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
    public ResponseEntity<List<ChatMessage>> getChatMessagesById(Integer id) {
        try {
            chatMessageService.initalizeChat(id);
            List<ChatMessage> cm = chatMessageService.getChatMessages();
            return ResponseEntity.ok(cm);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<ChatMessage> createChatMessage(NewChatMessage newChatMessage) {
        try {
            chatMessageService.initalizeChat(newChatMessage.getChatRoomId());
            chatMessageService.createChatMessage(newChatMessage);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}