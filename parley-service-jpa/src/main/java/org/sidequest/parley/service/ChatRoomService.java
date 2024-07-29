package org.sidequest.parley.service;

import org.sidequest.parley.entity.ChatRoomEntity;
import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.helpers.FileSystemHelper;
import org.sidequest.parley.mapper.ChatRoomMapper;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.NewChatRoom;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ChatRoomService {
    private static final Logger log = Logger.getLogger(ChatRoomService.class.getName());

    private ChatRoomRepository chatRoomRepository;

    // @Autowired
    private EnrollmentService enrollmentService;
    //@Autowired
    private UserService userService;

    @Autowired
    private FileSystemHelper fileSystemHelper;

    @Value("${chatroom.icon.directory}")
    private String chatroomIconDirectory;

    @Autowired
    public void setEnrollmentService(@Lazy EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @Autowired
    public void setUserService(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setChatRoomRepository(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }


    public List<ChatRoom> getChatRooms() {
        log.info("Attempting to retrieve all chat rooms");
        List<ChatRoom> chatRooms = new ArrayList<>();
        List<ChatRoomEntity> chatRoomEntities = chatRoomRepository.findAll();
        for (ChatRoomEntity chatRoomEntity : chatRoomEntities) {
            log.info("chatRoom: " + chatRoomEntity.getName() + " (" + chatRoomEntity.getId() + ")");
            ChatRoom chatRoom = ChatRoomMapper.INSTANCE.toModel(chatRoomEntity);
            // We need to get the enrolled users for the chat room
            // Then we add the users to the chat room
            enrollmentService.getChatRoomByUserIds(chatRoomEntity.getId()).forEach(userId -> {
                chatRoom.addUsersItem(userService.getUser(userId));
            });

            chatRooms.add(chatRoom);
        }
        log.info("Retrieved all chatrooms size: " + chatRooms.size());
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

        // We need to get the enrolled users for the chat room
        // Then we add the users to the chat room
        enrollmentService.getChatRoomByUserIds(id).forEach(userId -> {
            chatRoom.addUsersItem(userService.getUser(userId));
        });

        log.info("Chat room: " + chatRoom.getName() + " (" + chatRoom.getChatRoomId() + ") has " + chatRoom.getUsers().size() + " users");
        log.info("Successfully retrieved chat room with id " + id);
        return chatRoom;
    }

    @Transactional
    public ChatRoom createChatRoom(NewChatRoom newChatRoom) {
        try {
            log.info("Attempting to create chat room: " + newChatRoom.getName());
            // Validate new chat room details
            if (newChatRoom == null || newChatRoom.getName() == null || newChatRoom.getName().isEmpty()) {
                log.warning("Invalid chat room details provided.");
                throw new IllegalArgumentException("Chat room name cannot be null or empty.");
            }
            UserEntity moderator = userService.getUserEntity(newChatRoom.getModerator().getId());

            // Create and save the new chat room entity
            ChatRoomEntity chatRoomEntity = new ChatRoomEntity();
            chatRoomEntity.setName(newChatRoom.getName());
            chatRoomEntity.setModerator(moderator);

            // Save and flush to get the id
            // if you just save, the id will be null because the entity manager has not persisted the entity yet
            // to the database. The flush method forces the entity manager to persist the entity to the database.
            // This is useful when you need the id of the entity immediately after saving it.
            // If you don't need the id immediately, you can just call save.
            // https://www.baeldung.com/spring-data-jpa-save-saveandflush
            log.info("Saving chat room entity: " + chatRoomEntity.getName());
            ChatRoomEntity resultChatroomEntity = chatRoomRepository.saveAndFlush(chatRoomEntity);

            log.info("Chat room entity created: " + resultChatroomEntity.getName() + " (" + resultChatroomEntity.getId() + ")");

            ChatRoom resultChatroom = ChatRoomMapper.INSTANCE.toModel(resultChatroomEntity);

            // Add the users to the chatroom including the moderator
            Set<User> userSet = new HashSet<>(newChatRoom.getUsers());
            userSet.add(newChatRoom.getModerator());
            List<User> uniqueUsers = new ArrayList<>(userSet);

            enrollmentService.addUsersToChatRoom(uniqueUsers, resultChatroom);

            log.info("Chat room created successfully: " + resultChatroom.getName());
            return getChatRoom(resultChatroom.getChatRoomId());

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to create chat room: " + newChatRoom.getName(), e);
            throw new RuntimeException("Failed to create chat room: " + newChatRoom.getName(), e);
        }
    }


    @Transactional
    public ChatRoom updateChatRoom(Long id, ChatRoom chatRoom) {
        log.info("Attempting to update chat room with id " + id);
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chat room with id " + id + " not found"));

        // Update the chat room details
        chatRoomEntity.setName(chatRoom.getName());
        chatRoomEntity.setModerator(userService.getUserEntity(chatRoom.getModerator().getId()));
        chatRoomRepository.saveAndFlush(chatRoomEntity); // Save and flush to persist to the database

        // Remove all users from the chat room and add the new users
        enrollmentService.removeUsersFromChatRoom(id);
        enrollmentService.addUsersToChatRoom(chatRoom.getUsers(), chatRoom);

        log.info("Successfully updated chat room with id " + id);
        return getChatRoom(id);
    }

    public boolean isUserInChatRoom(Long userId, Long chatRoomId) {
        log.info("Checking if user: " + userId + " is in chat room: " + chatRoomId);
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        if (chatRoom == null) {
            log.severe("Chat room with id " + chatRoomId + " not found");
            return false;
        }

        for (User user : chatRoom.getUsers()) {
            if (user.getId().equals(userId)) {
                log.info("User: " + userId + " is in chat room: " + chatRoomId);
                return true;
            }
        }

        log.info("User: " + userId + " is not in chat room: " + chatRoomId);
        return false;
    }

    public String getChatRoomIcon(Long id) throws Exception {
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(id).orElseThrow(() -> new Exception("Chatroom not found"));
        return chatRoomEntity.getIconPath();
    }

    @Transactional
    public void setChatRoomIcon(Long chatroomId, MultipartFile iconFile) throws Exception {
        if (!iconFile.isEmpty()) {
            log.fine("Saving icon for chatroom: " + chatroomId);
            ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatroomId).orElseThrow(() -> new Exception("Chatroom not found"));
            String path = fileSystemHelper.saveFile(iconFile, chatroomIconDirectory);
            chatRoomEntity.setIconPath(path);
            chatRoomRepository.save(chatRoomEntity);
        } else {
            log.severe("Empty file!");
            throw new Exception("Empty file!");
        }
    }

    public void deleteChatRoom(Long id) {
        log.info("Attempting to delete chat room with id " + id);
        chatRoomRepository.deleteById(id);
        log.info("Successfully deleted chat room with id " + id);
    }

    public ChatRoomEntity getChatRoomEntity(Long chatRoomId) {
        log.info("Attempting to retrieve ChatRoomEntity with id " + chatRoomId);

        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId)
                .orElse(null);

        if (chatRoomEntity == null) {
            log.warning("ChatRoomEntity with id " + chatRoomId + " not found");
            return null;
        }

        log.info("Successfully retrieved ChatRoomEntity with id " + chatRoomId);
        return chatRoomEntity;
    }

}