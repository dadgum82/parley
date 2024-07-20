package org.sidequest.parley.controller;


import org.sidequest.parley.api.ChatroomsApi;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.NewChatRoom;
import org.sidequest.parley.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;


/**
 * Controller class for managing chat rooms.
 */
@RestController
public class ChatRoomController implements ChatroomsApi {
    private static final Logger log = Logger.getLogger(ChatRoomController.class.getName());

    @Autowired
    private ChatRoomService chatRoomService;

    @Override
    public ResponseEntity<List<ChatRoom>> getChatRooms() {

        try {

            List<ChatRoom> chatRooms = chatRoomService.getChatRooms();
            for (ChatRoom chatRoom : chatRooms) {
                log.info("**** chatRoom: " + chatRoom.getName());
            }
            return ResponseEntity.ok(chatRooms);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<ChatRoom> getChatRoomById(Long id) {
        try {
            ChatRoom chatRoom = chatRoomService.getChatRoom(id);
            return ResponseEntity.ok(chatRoom);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<ChatRoom> createChatRoom(NewChatRoom newChatRoom) {
        try {
            ChatRoom chatRoom = chatRoomService.createChatRoom(newChatRoom);
            if (chatRoom != null) {
                return new ResponseEntity<>(chatRoom, HttpStatus.CREATED);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.severe("Error creating chat room: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<ChatRoom> updateChatRoom(Long id, ChatRoom chatRoom) {
        try {
            ChatRoom resultChatRoom = chatRoomService.updateChatRoom(id, chatRoom);
            if (resultChatRoom != null) {
                return new ResponseEntity<>(resultChatRoom, HttpStatus.CREATED);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.severe("Error creating chat room: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}