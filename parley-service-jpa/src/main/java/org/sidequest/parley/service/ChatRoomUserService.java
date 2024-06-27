package org.sidequest.parley.service;

import org.sidequest.parley.entity.ChatRoomEntity;
import org.sidequest.parley.entity.ChatRoomsUsersEntity;
import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.repository.ChatRoomRepository;
import org.sidequest.parley.repository.ChatRoomsUsersRepository;
import org.sidequest.parley.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ChatRoomUserService {
    private static final Logger log = Logger.getLogger(ChatRoomUserService.class.getName());

    @Autowired
    private ChatRoomsUsersRepository chatRoomsUsersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;


    public ChatRoomUserService() {

    }

    public boolean isUserInChatRoom(Long userId, Long chatRoomId) {

        log.info("Checking if user with id " + userId + " is in chat room with id " + chatRoomId);
        Optional<ChatRoomsUsersEntity> chatRoomsUsersEntity = chatRoomsUsersRepository.findByUserIdAndChatRoomId(userId, chatRoomId);
        if (chatRoomsUsersEntity.isPresent()) {
            log.info("User with id " + userId + " is in chat room with id " + chatRoomId);
            return true;
        }
        return false;
    }

    public boolean isUserModeratorInChatRoom(Long userId, Long chatRoomId) {
        log.info("Checking if user with id " + userId + " is a moderator in chat room with id " + chatRoomId);
        Optional<ChatRoomsUsersEntity> chatRoomsUsersEntity = chatRoomsUsersRepository.findByUserIdAndChatRoomId(userId, chatRoomId);
        if (chatRoomsUsersEntity.isPresent() && chatRoomsUsersEntity.get().getModerator() != null) {
            log.info("User with id " + userId + " is a moderator in chat room with id " + chatRoomId);
            return true;
        }
        return false;
    }

    @Transactional
    public void addUserToChatRoom(Long userId, Long chatRoomId) {
        log.info("Attempting to add user with id " + userId + " to chat room with id " + chatRoomId);

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> {
            log.severe("User with id " + userId + " not found");
            return new RuntimeException("User not found");
        });

        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> {
            log.severe("ChatRoom with id " + chatRoomId + " not found");
            return new RuntimeException("ChatRoom not found");
        });

        ChatRoomsUsersEntity chatRoomsUsersEntity = new ChatRoomsUsersEntity();
        chatRoomsUsersEntity.setUser(userEntity);
        chatRoomsUsersEntity.setChatRoom(chatRoomEntity);

        chatRoomsUsersRepository.save(chatRoomsUsersEntity);

        log.info("Successfully added user with id " + userId + " to chat room with id " + chatRoomId);
    }

    public void addUserToChatRoom(Long userId, Long chatRoomId, Long moderatorId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserEntity moderatorEntity = userRepository.findById(moderatorId).orElseThrow(() -> new RuntimeException("Moderator not found"));
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        ChatRoomsUsersEntity chatRoomsUsersEntity = new ChatRoomsUsersEntity();
        chatRoomsUsersEntity.setUser(userEntity);
        chatRoomsUsersEntity.setChatRoom(chatRoomEntity);
        chatRoomsUsersEntity.setModerator(moderatorEntity);
        chatRoomsUsersRepository.save(chatRoomsUsersEntity);
    }

    @Transactional
    public void removeAllUsersFromChatRoom(Long chatRoomId) {
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> {
            log.severe("ChatRoom with id " + chatRoomId + " not found");
            return new RuntimeException("ChatRoom not found");
        });
        chatRoomsUsersRepository.deleteAll(chatRoomEntity.getChatRoomUsers());


        //chatRoomsUsersRepository.deleteAllByChatRoomId(chatRoomId);
    }

    @Transactional
    public void removeUserFromChatRoom(Long userId, Long chatRoomId) {
        Optional<ChatRoomsUsersEntity> chatRoomsUsersEntity = chatRoomsUsersRepository.findByUserIdAndChatRoomId(userId, chatRoomId);
        if (chatRoomsUsersEntity.isPresent()) {
            chatRoomsUsersRepository.delete(chatRoomsUsersEntity.get());
        }
    }

}