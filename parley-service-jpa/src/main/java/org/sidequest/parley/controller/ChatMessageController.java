package org.sidequest.parley.controller;

import org.sidequest.parley.api.ChatsApi;
import org.sidequest.parley.model.ChatMessage;
import org.sidequest.parley.model.NewChatMessage;
import org.sidequest.parley.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class ChatMessageController implements ChatsApi {
    private static final Logger log = Logger.getLogger(ChatMessageController.class.getName());

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

            ChatMessage chatMessage = chatMessageService.createChatMessage(newChatMessage);
            if (chatMessage == null) {
                return ResponseEntity.notFound().build();
            }
            return new ResponseEntity<>(chatMessage, HttpStatus.CREATED);
        } catch (Exception e) {
            log.severe("Error creating chat message:" + e);
            return ResponseEntity.notFound().build();
        }
    }
}