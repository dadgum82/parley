package org.sidequest.parley.service;

import org.sidequest.parley.entity.ChatRoomEntity;
import org.sidequest.parley.mapper.ChatRoomMapper;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.ChatRoomRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ChatRoomService {
    private static final Logger log = Logger.getLogger(ChatRoomService.class.getName());


    private ChatRoomRepository chatRoomRepository;
    private EnrollmentService enrollmentService;

    //@Autowired
    public void setChatRoomRepository(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    // @Autowired
    public void setEnrollmentService(@Lazy EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    public List<ChatRoom> getChatRooms() {
        log.info("Attempting to retrieve all chat rooms");
        List<ChatRoom> chatRooms = new ArrayList<>();
        List<ChatRoomEntity> chatRoomEntities = chatRoomRepository.findAll();
        for (ChatRoomEntity chatRoomEntity : chatRoomEntities) {
            log.info("chatRoom: " + chatRoomEntity.getName() + " (" + chatRoomEntity.getId() + ")");
            ChatRoom chatRoom = ChatRoomMapper.INSTANCE.toModel(chatRoomEntity);

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

        List<User> tempUsers = new ArrayList<>();
        log.info("Retrieving users to chat room: " + chatRoom.getName() + " (" + chatRoom.getChatRoomId() + ")");
        for (User user : chatRoom.getUsers()) {
            log.info("\tchatRoom.getUsers(): " + user.toString());

        }

        log.info("Chat room: " + chatRoom.getName() + " (" + chatRoom.getChatRoomId() + ") has " + tempUsers.size() + " users");
        //chatRoom.setUsers(tempUsers);
        log.info("Successfully retrieved chat room with id " + id);
        return chatRoom;
    }

    @Transactional
    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        log.info("Attempting to create chat room: " + chatRoom.getName());

        // Convert ChatRoom model to entity
        ChatRoomEntity chatRoomEntity = ChatRoomMapper.INSTANCE.toEntity(chatRoom);

        // Save the ChatRoomEntity to get the generated ID
        chatRoomEntity = chatRoomRepository.save(chatRoomEntity);

// Convert the saved ChatRoomEntity back to model
        ChatRoom resultChatRoom = ChatRoomMapper.INSTANCE.toModel(chatRoomEntity);


        enrollmentService.addUsersToChatRoom(chatRoom.getUsers(), resultChatRoom);


        // TODO: The resultChatRoom object might not contain the user list correctly
        // Fetch the chat room again to get the correct user list
        return getChatRoom(resultChatRoom.getChatRoomId());
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


}