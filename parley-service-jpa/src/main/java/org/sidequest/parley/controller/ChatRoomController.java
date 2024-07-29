package org.sidequest.parley.controller;


import org.sidequest.parley.api.ChatroomsApi;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.Error;
import org.sidequest.parley.model.NewChatRoom;
import org.sidequest.parley.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
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

    @Override
    public ResponseEntity<Error> deleteChatRoom(Long id) {
        try {
            chatRoomService.deleteChatRoom(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> setChatRoomIcon(Long id, MultipartFile file) {
        try {
            chatRoomService.setChatRoomIcon(id, file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Resource> getChatRoomIcon(Long id) {
        try {
            String avatarPath = chatRoomService.getChatRoomIcon(id);
            File file = new File(avatarPath);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + file.getName());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.IMAGE_PNG) // or MediaType.IMAGE_PNG if it's a PNG image MediaType.IMAGE_JPEG
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}