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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Controller class for managing chat rooms.
 */
@RestController

public class ChatRoomController implements ChatroomsApi {

    @Autowired
    ChatRoomService chatRoomService;

    @Override
    public ResponseEntity<List<ChatRoom>> getChatRooms() {

        try {
            List<ChatRoom> chatRooms = chatRoomService.getChatRooms();
            return ResponseEntity.ok(chatRooms);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<ChatRoom> getChatRoomById(Integer id) {
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
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setName(newChatRoom.getName());
            chatRoom.setModeratorId(newChatRoom.getModeratorId());

            ChatRoom resultingChatRoom = chatRoomService.createChatRoom(chatRoom);
            return ResponseEntity.ok(resultingChatRoom);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Error> deleteChatRoom(Integer id) {
        try {
            chatRoomService.deleteChatRoom(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<ChatRoom> updateChatRoom(Integer id, ChatRoom chatRoom) {
        try {
            ChatRoom updatedChatRoom = chatRoomService.updateChatRoom(chatRoom);
            return ResponseEntity.ok(updatedChatRoom);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> setChatRoomIcon(Integer id, MultipartFile file) {
        try {
            chatRoomService.setChatRoomIcon(id, file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Resource> getChatRoomIcon(Integer id) {
        try {
            String iconPath = chatRoomService.getChatRoomIcon(id);
            File file = new File(iconPath);
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