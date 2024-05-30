package org.sidequest.parley.service;

import org.sidequest.parley.entity.ChatRoomsUsersEntity;
import org.sidequest.parley.repository.ChatRoomsUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ChatRoomUserService {
    Logger log = Logger.getLogger(ChatRoomUserService.class.getName());
    @Autowired
    private ChatRoomsUsersRepository repository;

    public ChatRoomUserService() {

    }

    public boolean isUserInChatRoom(Long userId, Long chatRoomId) {

        log.info("Checking if user with id " + userId + " is in chat room with id " + chatRoomId);
        Optional<ChatRoomsUsersEntity> chatRoomsUsersEntity = repository.findByUserIdAndChatRoomId(userId, chatRoomId);
        if (chatRoomsUsersEntity.isPresent()) {
            log.info("User with id " + userId + " is in chat room with id " + chatRoomId);
            return true;
        }
        return false;
    }
}