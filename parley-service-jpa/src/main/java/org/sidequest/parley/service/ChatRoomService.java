package org.sidequest.parley.service;

import jakarta.transaction.Transactional;
import org.sidequest.parley.entity.ChatRoomEntity;
import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.mapper.ChatRoomMapper;
import org.sidequest.parley.mapper.UserMapper;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.ChatRoomRepository;
import org.sidequest.parley.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ChatRoomService {
    private static final Logger log = Logger.getLogger(ChatRoomService.class.getName());

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    ChatRoomUserService chatRoomUserService;


    @Value("${chatroom.icon.directory}")
    private String uploadDir;

    public ChatRoomService() {
    }

    public List<ChatRoom> getChatRooms() {
        List<ChatRoom> chatRooms = new ArrayList<>();
        List<ChatRoomEntity> chatRoomEntities = chatRoomRepository.findAll();
        for (ChatRoomEntity chatRoomEntity : chatRoomEntities) {
            log.info("chatRoom: " + chatRoomEntity.getName() + " (" + chatRoomEntity.getId() + ")");
            ChatRoom chatRoom = ChatRoomMapper.INSTANCE.toModel(chatRoomEntity);
            chatRoom.setUsers(new ArrayList<>());
            for (UserEntity user : chatRoomEntity.getUsers()) {
                User u = UserMapper.INSTANCE.toModel(user);
                chatRoom.addUsersItem(u);
            }
            chatRooms.add(chatRoom);
        }
        log.info("chatRooms: " + chatRooms.size());
        return chatRooms;
    }

    public ChatRoom getChatRoom(Long id) {
        log.info("Attempting to retrieve chat room with id " + id);
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .map(ChatRoomMapper.INSTANCE::toModel).orElse(null);

        if (chatRoom == null) {
            log.severe("Chat room with id " + id + " not found");
            return null;
        }

        List<User> tempUsers = new ArrayList<>();
        log.info("Retrieving users to chat room: " + chatRoom.getName() + " (" + chatRoom.getChatRoomId() + ")");
        for (User user : chatRoom.getUsers()) {
            log.info("\tchatRoom.getUsers(): " + user.toString());
//            UserEntity goodUser = userRepository.findById(user.getId()).orElseThrow(() -> {
//                log.severe("User with id " + user.getId() + " not found");
//                return new RuntimeException("User not found");
//            });
//            //tempUsers.add(UserMapper.INSTANCE.toModel(goodUser));
//            chatRoom.addUsersItem(UserMapper.INSTANCE.toModel(goodUser));
        }

        log.info("Chat room: " + chatRoom.getName() + " (" + chatRoom.getChatRoomId() + ") has " + tempUsers.size() + " users");
        //chatRoom.setUsers(tempUsers);
        log.info("Successfully retrieved chat room with id " + id);
        return chatRoom;
    }

    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        ChatRoomEntity chatRoomEntity = ChatRoomMapper.INSTANCE.toEntity(chatRoom);
        ChatRoomEntity temp = ChatRoomMapper.INSTANCE.toEntity(chatRoom);

        chatRoomEntity.setName(temp.getName());
        chatRoomEntity.setModerator(temp.getModerator());
        chatRoomEntity.setChatRoomUsers(temp.getChatRoomUsers());

        chatRoomEntity = chatRoomRepository.save(chatRoomEntity);
        ChatRoom resultChatRoom = ChatRoomMapper.INSTANCE.toModel(chatRoomEntity);
        log.info("ChatRoom created: " + resultChatRoom.getChatRoomId());
        log.info("ChatRoom name: " + resultChatRoom.getName());
        for (User user : chatRoom.getUsers()) {
            log.info("\tAdding user: " + user.getId() + " to chat room: " + resultChatRoom.getChatRoomId());
            chatRoomUserService.addUserToChatRoom(user.getId(), resultChatRoom.getChatRoomId());
        }

        //TODO: The resultChatRoom object is not being returned with the correct user list
        // This is a workaround to get the correct user list
        return getChatRoom(resultChatRoom.getChatRoomId());
    }

    @Transactional
    public ChatRoom updateChatRoom(ChatRoom chatRoom) {
        try {
            ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoom.getChatRoomId()).orElseThrow(() ->
                    new RuntimeException("ChatRoom not found"));

            ChatRoomEntity temp = ChatRoomMapper.INSTANCE.toEntity(chatRoom);

            log.info("Updating chat room: " + chatRoom.getChatRoomId());
            log.info("Chat room name: " + chatRoom.getName());
            log.info("Chat room moderator: " + chatRoom.getModerator().toString());
            chatRoomEntity.setName(temp.getName());
            chatRoomEntity.setModerator(temp.getModerator());
            chatRoomEntity.setChatRoomUsers(null);
            chatRoomUserService.removeAllUsersFromChatRoom(temp.getId());

            chatRoomEntity = chatRoomRepository.save(chatRoomEntity);
            ChatRoom resultChatRoom = ChatRoomMapper.INSTANCE.toModel(chatRoomEntity);
            for (User user : chatRoom.getUsers()) {
                log.info("Adding user: " + user.getId() + " to chat room: " + resultChatRoom.getChatRoomId());
                chatRoomUserService.addUserToChatRoom(user.getId(), resultChatRoom.getChatRoomId());
            }

            //TODO: The resultChatRoom object is not being returned with the correct user list
            // This is a workaround to get the correct user list
            return getChatRoom(resultChatRoom.getChatRoomId());
        } catch (Exception e) {
            log.severe("Error updating chat room: " + e.getMessage());
            return null;
        }
    }

    public void deleteChatRoom(Long chatRoomId) {
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        chatRoomRepository.delete(chatRoomEntity);
    }

    public void setChatRoomIcon(Long chatRoomId, MultipartFile file) throws IOException {
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            String fileName = chatRoomEntity.getId() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName); // replace with your path
            Files.write(path, bytes);

            chatRoomEntity.setIconPath(path.toString());
            chatRoomRepository.save(chatRoomEntity);
        } else {
            throw new RuntimeException("Empty file!");
        }
    }

    public String getChatRoomIcon(Long chatRoomId) {
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        return chatRoomEntity.getIconPath();
    }

}