package org.sidequest.parley.service;

import org.sidequest.parley.entity.ChatRoomEntity;
import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.mapper.ChatRoomMapper;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.ChatRoomRepository;
import org.sidequest.parley.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {
    private static final Logger log = LoggerFactory.getLogger(ChatRoomService.class);

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${chatroom.icon.directory}")
    private String uploadDir;

    public ChatRoomService() {
    }

    public List<ChatRoom> getChatRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll().stream()
                .map(ChatRoomMapper.INSTANCE::toModel)
                .collect(Collectors.toList());

        for (ChatRoom chatRoom : chatRooms) {
            for (User user : chatRoom.getUsers()) {
                UserEntity goodUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
                user.setName(goodUser.getName());
            }
        }
        return chatRooms;
    }

    public ChatRoom getChatRoom(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .map(ChatRoomMapper.INSTANCE::toModel).orElse(null);
        for (User user : chatRoom.getUsers()) {
            UserEntity goodUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
            user.setName(goodUser.getName());
        }
        return chatRoom;
    }

    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        ChatRoomEntity chatRoomEntity = ChatRoomMapper.INSTANCE.toEntity(chatRoom);
        chatRoomEntity = chatRoomRepository.save(chatRoomEntity);
        return ChatRoomMapper.INSTANCE.toModel(chatRoomEntity);
    }

    public ChatRoom updateChatRoom(ChatRoom chatRoom) {
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoom.getChatRoomId()).orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        ChatRoomEntity temp = ChatRoomMapper.INSTANCE.toEntity(chatRoom);

        chatRoomEntity.setName(temp.getName());
        chatRoomEntity.setModerator(temp.getModerator());
        chatRoomEntity.setChatRoomUsers(temp.getChatRoomUsers());
        chatRoomEntity = chatRoomRepository.save(chatRoomEntity);
        return ChatRoomMapper.INSTANCE.toModel(chatRoomEntity);
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