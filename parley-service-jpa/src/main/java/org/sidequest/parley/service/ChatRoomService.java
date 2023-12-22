package org.sidequest.parley.service;

// other imports...

import org.sidequest.parley.mapper.ChatRoomMapper;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {
    @Autowired
    ChatRoomUsersRepository chatRoomUsersRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;
    @Autowired
    UserRepository userRepository;
    @Value("${chatroom.icon.directory}")
    private String uploadDir;

    public ChatRoomService() {
    }

    public List<ChatRoom> getChatRooms() {
        return chatRoomRepository.findAll().stream()
                .map(ChatRoomMapper.INSTANCE::mapTo)
                .collect(Collectors.toList());
    }

    public ChatRoom getChatRoom(int chatRoomId) {
        return chatRoomRepository.findById((long) chatRoomId)
                .map(ChatRoomMapper.INSTANCE::mapTo)
                .orElse(null);
    }

    @Transactional
    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        ChatRoomEntity chatRoomEntity = ChatRoomMapper.INSTANCE.mapTo(chatRoom);

        // Handle the users field
        ChatRoomEntity finalChatRoomEntity = chatRoomEntity;
        List<ChatRoomUsersEntity> chatRoomUsers = chatRoom.getUsers().stream()
                .map(user -> {
                    UserEntity userEntity = userRepository.findById((long) user.getId())
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    ChatRoomUsersEntity chatRoomUser = new ChatRoomUsersEntity();
                    chatRoomUser.setUser(userEntity);
                    chatRoomUser.setChatRoom(finalChatRoomEntity);
                    chatRoomUsersRepository.save(chatRoomUser);
                    return chatRoomUser;
                })
                .collect(Collectors.toList());

        chatRoomEntity.setChatRoomUsers(chatRoomUsers);

        chatRoomEntity = chatRoomRepository.save(chatRoomEntity);
        return ChatRoomMapper.INSTANCE.mapTo(chatRoomEntity);
    }

    @Transactional
    public ChatRoom updateChatRoom(ChatRoom chatRoom) {
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById((long) chatRoom.getChatRoomId())
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        chatRoomEntity.setName(chatRoom.getName());

        // Handle the users field
        ChatRoomEntity finalChatRoomEntity = chatRoomEntity;
        List<ChatRoomUsersEntity> chatRoomUsers = chatRoom.getUsers().stream()
                .map(user -> {
                    UserEntity userEntity = userRepository.findById((long) user.getId())
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    ChatRoomUsersEntity chatRoomUser = new ChatRoomUsersEntity();
                    chatRoomUser.setUser(userEntity);
                    chatRoomUser.setChatRoom(finalChatRoomEntity);
                    chatRoomUsersRepository.save(chatRoomUser);
                    return chatRoomUser;
                })
                .collect(Collectors.toList());

        chatRoomEntity.setChatRoomUsers(chatRoomUsers);

        chatRoomEntity = chatRoomRepository.save(chatRoomEntity);
        return ChatRoomMapper.INSTANCE.mapTo(chatRoomEntity);
    }


    @Transactional
    public void deleteChatRoom(int chatRoomId) {
        chatRoomRepository.deleteById((long) chatRoomId);
    }

    public ChatRoom findChatRoomById(int chatRoomId) {
        return chatRoomRepository.findById((long) chatRoomId)
                .map(ChatRoomMapper.INSTANCE::mapTo)
                .orElse(null);
    }

    public void setChatRoomIcon(int chatRoomId, MultipartFile file) throws Exception {
        ChatRoomEntity chatRoom = chatRoomRepository.findById((long) chatRoomId).orElseThrow(() -> new Exception("Chat room not found"));

        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            String fileName = chatRoom.getId() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, bytes);

            chatRoom.setIconPath(path.toString());
            chatRoomRepository.save(chatRoom);
        } else {
            throw new Exception("Empty file!");
        }
    }

    public String getChatRoomIcon(int chatRoomId) throws Exception {
        ChatRoomEntity chatRoom = chatRoomRepository.findById((long) chatRoomId).orElseThrow(() -> new Exception("Chat room not found"));
        return chatRoom.getIconPath();
    }
}


/////////////////////////// 12/21/2023 ///////////////////////////


//package org.sidequest.parley.service;
//
//
//import jakarta.annotation.PostConstruct;
//import org.sidequest.parley.mapper.ChatRoomMapper;
//import org.sidequest.parley.model.ChatRoom;
//import org.sidequest.parley.repository.ChatRoomEntity;
//import org.sidequest.parley.repository.ChatRoomRepository;
//import org.sidequest.parley.repository.UserEntity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class ChatRoomService {
//    private static final Logger log = LoggerFactory.getLogger(ChatRoomService.class);
//
//    private int CHAT_ROOM_COUNT;
//    private List<ChatRoom> chatRooms;
//
//    @Value("${chatroom.icon.directory}")
//    private String uploadDir;
//
//    @Autowired
//    ChatRoomRepository chatRoomRepository;
//
//    public ChatRoomService() {
//
//    }
//@PostConstruct
//    private void initalizeChatRooms() {
//        this.CHAT_ROOM_COUNT = 0;
//        this.chatRooms = new ArrayList<>();
//
//        chatRoomRepository.findAll().forEach(chatRoomEntity -> {
//           ChatRoom chatRoom = ChatRoomMapper.INSTANCE.mapTo(chatRoomEntity);
//            this.chatRooms.add(chatRoom);
//        });
//        this.CHAT_ROOM_COUNT = chatRooms.size();
//    }
//
//    public List<ChatRoom> getChatRooms() {
//        return chatRooms;
//    }
//
//    public ChatRoom getChatRoom(int chatRoomId) {
//        for (ChatRoom c : this.chatRooms) {
//            if (c.getChatRoomId() == chatRoomId) {
//                return c;
//            }
//        }
//        return null;
//    }
//
//    public ChatRoom getChatRoom(String name) {
//        for (ChatRoom c : this.chatRooms) {
//            if (c.getName().equalsIgnoreCase(name)) {
//                return c;
//            }
//        }
//        return null;
//    }
//
//
//// --Commented out by Inspection START (11/26/2023 8:06 AM):
////    public ChatRoom createChatRoom(String name, int moderatorId) throws SQLException {
////        return createChatRoom(name, moderatorId, null);
////    }
//// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
//
//    public ChatRoom createChatRoom(String name, int moderatorId) throws SQLException {
//        return createChatRoom(name, moderatorId, null);
//    }
//
//    public ChatRoom createChatRoom(String name, int moderatorId, List<Integer> userIds) throws SQLException {
//        if (name == null || name.isEmpty()) {
//            throw new IllegalArgumentException("Chat room name cannot be null or empty");
//        }
//
//        int chatRoomCount = this.CHAT_ROOM_COUNT;
//        chatRoomCount++; //new user increment count
//        System.out.println("Creating chat room with id: " + chatRoomCount);
//        System.out.println("Chat room name: " + name);
//        System.out.println("Chat room moderator id: " + moderatorId);
//        if (userIds != null) {
//            System.out.println("Chat room user id Size: " + userIds.size());
//            System.out.println("Chat room user ids: " + userIds);
//        }
//
//        ChatRoom chatRoom = initializeChatRoomHelper(chatRoomCount, name, moderatorId, userIds);
//        chatRoom = addModeratorToUserIDs(chatRoom, moderatorId, userIds);
//        ChatRoomEntity chatRoomEntity = ChatRoomMapper.INSTANCE.mapTo(chatRoom);
//
//        chatRoomRepository.save(chatRoomEntity);
//        this.chatRooms.add(chatRoom);
//        return chatRoom;
//    }
//
//    private ChatRoom addModeratorToUserIDs(ChatRoom chatRoom, int moderatorId, List<Integer> userIds) {
//        if (userIds == null || userIds.isEmpty()) {
//            chatRoom.addUserIdsItem(moderatorId);
//            // chatRoom.addUserId(moderatorId); // this is the old code
//        } else if (!userIds.contains(moderatorId)) {
//            chatRoom.addUserIdsItem(moderatorId);
//            // chatRoom.addUserId(moderatorId);
//        }
//        return chatRoom;
//    }
//
//    private ChatRoom initializeChatRoomHelper(int chatRoomId, String name, int moderatorId, List<Integer> userIds) {
//        ChatRoom chatRoom = new ChatRoom();
//        chatRoom.setChatRoomId(chatRoomId);
//        chatRoom.setName(name);
//        chatRoom.setModeratorId(moderatorId);
//
//        if (userIds != null && !userIds.isEmpty()) {
//            chatRoom.setUserIds(userIds);
//        }
//
//        return chatRoom;
//    }
//
//
//    /**
//     * Delete a chat room by id
//     *
//     * @param chatId
//     * @return true if chat room was deleted, false if chat room was not found
//     */
//    public boolean deleteChatRoom(int chatId) {
//        ChatRoom chatRoom = this.getChatRoom(chatId);
//        if (chatRoom != null) {
//
//            this.chatRooms.remove(chatRoom);
//            ChatRoomEntity chatRoomEntity = ChatRoomMapper.INSTANCE.mapTo(chatRoom);
//            chatRoomRepository.delete(chatRoomEntity);
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * Update a chat room by id and name
//     *
//     * @param chatId
//     * @param name
//     * @return ChatRoom if chat room was updated
//     */
//    public ChatRoom updateChatRoom(int chatId, String name) {
//        ChatRoom chatRoom = this.getChatRoom(chatId);
//        if (chatRoom != null) {
//            chatRoom.setName(name);
//            ChatRoomEntity chatRoomEntity = ChatRoomMapper.INSTANCE.mapTo(chatRoom);
//            chatRoomRepository.save(chatRoomEntity);
//        }
//        return chatRoom;
//    }
//
//    public ChatRoom updateChatRoom(int chatRoomId, String name, int moderatorId) {
//        ChatRoom chatRoom = this.getChatRoom(chatRoomId);
//        if (chatRoom != null) {
//            chatRoom.setChatRoomId(chatRoomId);
//            chatRoom.setName(name);
//            chatRoom.setModeratorId(moderatorId);
//            ChatRoomEntity chatRoomEntity = ChatRoomMapper.INSTANCE.mapTo(chatRoom);
//            chatRoomRepository.save(chatRoomEntity);
//        }
//        return chatRoom;
//    }
//
//    public void setChatRoomIcon(int chatRoomId, MultipartFile file) throws Exception {
//        ChatRoomEntity chatRoom = chatRoomRepository.findById((long) chatRoomId).orElseThrow(() -> new Exception("Chat room not found"));
//
//        if (!file.isEmpty()) {
//            byte[] bytes = file.getBytes();
//            String fileName = chatRoom.getId() + "_" + file.getOriginalFilename();
//            Path path = Paths.get(uploadDir + fileName);
//            Files.write(path, bytes);
//
//            chatRoom.setIconPath(path.toString());
//            chatRoomRepository.save(chatRoom);
//        } else {
//            throw new Exception("Empty file!");
//        }
//    }
//
//    public String getChatRoomIcon(int chatRoomId) throws Exception {
//        ChatRoomEntity chatRoom = chatRoomRepository.findById((long) chatRoomId).orElseThrow(() -> new Exception("Chat room not found"));
//        return chatRoom.getIconPath();
//    }
//
//}
