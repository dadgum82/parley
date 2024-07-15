package org.sidequest.parley.service;

import org.sidequest.parley.entity.ChatMessageEntity;
import org.sidequest.parley.mapper.ChatMessageMapper;
import org.sidequest.parley.model.ChatMessage;
import org.sidequest.parley.model.NewChatMessage;
import org.sidequest.parley.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {
    private static final Logger log = Logger.getLogger(ChatMessageService.class.getName());

    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessageService(ChatRoomService chatRoomService, UserService userService, ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessageRepository.findAll().stream().map(ChatMessageMapper.INSTANCE::toModel).collect(Collectors.toList());
    }

    public ChatMessage getChatMessage(Long id) {
        ChatMessage chatMessage = chatMessageRepository.findById(id).map(ChatMessageMapper.INSTANCE::toModel).orElse(null);
        log.fine("getChatMessage: " + chatMessage);

        if (chatMessage == null) {
            return null;
        }


        //
//        assert chatMessage != null;
//        User user = chatMessage.getUser();
//        UserEntity goodUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
//        user.setName(goodUser.getName());
//
//        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatMessage.getChatRoom().getChatRoomId()).orElseThrow(() -> new RuntimeException("ChatRoom not found"));
//        chatRoom.setName(chatRoom.getName());
//
//        // TODO: Fix the mapper so we don't have to do this manually rebuilt of the object
//        ArrayList<User> goodChatRoomUsers = new ArrayList<>();
//        for (User chatRoomUser : chatMessage.getChatRoom().getUsers()) {
//            UserEntity userEntity = userRepository.findById(chatRoomUser.getId()).orElseThrow(() -> new RuntimeException("User not found"));
//            goodChatRoomUsers.add(new User().id(userEntity.getId()).name(userEntity.getName()));
//        }
//        chatMessage.getChatRoom().setUsers(goodChatRoomUsers);
        return chatMessage;
    }

    @Transactional
    public ChatMessage createChatMessage(NewChatMessage newChatMessage) throws SQLException {
        log.fine("createChatMessage: " + newChatMessage);
        ChatMessage cm = new ChatMessage();

        // We get the current time in OffsetDateTime format
        // The mapper will convert it to UTC for storage in the database
        // The mapper will convert it back to the user's timezone when the chat message is retrieved
        OffsetDateTime odt = OffsetDateTime.now();

        ChatRoomService chatRoomService = new ChatRoomService();
        UserService userService = new UserService();
        Long chatRoomId = newChatMessage.getChatRoomId();
        Long userId = newChatMessage.getUserId();

        // Check if the user is a member of the chat room
        if (chatRoomService.isUserInChatRoom(userId, chatRoomId)) {
            log.fine("User is a member of the chat room");
        } else {
            throw new RuntimeException("Access denied: User is not a member of the chat room");
        }

        // Retrieve the user's timezone
        String strUserTimezone = userService.getUserTimezone(userId);
        log.fine("User timezone: " + strUserTimezone);

        cm.setTimestamp(odt);
        cm.setChatRoom(chatRoomService.getChatRoom(chatRoomId));
        cm.setUser(userService.getUser(userId));
        cm.setContent(newChatMessage.getContent());

        ChatMessageEntity chatMessageEntity = ChatMessageMapper.INSTANCE.toEntity(cm);
        chatMessageEntity = chatMessageRepository.save(chatMessageEntity);

        userService.updateLastPostedMessageDateTime(userId, odt);
        return ChatMessageMapper.INSTANCE.toModel(chatMessageEntity);
    }

//    @Transactional
//    public ChatMessage createChatMessage(NewChatMessage newChatMessage) throws SQLException {
//        log.fine("createChatMessage: " + newChatMessage);
//        ChatMessage cm = new ChatMessage();
//        ChatRoomService chatRoomService = new ChatRoomService();
//        UserService userService = new UserService();
//        Long chatRoomId = newChatMessage.getChatRoomId();
//        Long userId = newChatMessage.getUserId();
//
//
//        // Check if the user is a member of the chat room
//        // TODO: Fix this check
////        if (!crs.isUserInChatRoom(userId, chatRoomId)) {
////            throw new RuntimeException("Access denied: User is not a member of the chat room");
////        }
//        log.fine("User is a member of the chat room");
//
//        // Retrieve the user's timezone
////        UserEntity userEntity = userRepository.findById(userId)
////                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // We get the current time in OffsetDateTime format
//        // The mapper will convert it to UTC for storage in the database
//        // The mapper will convert it back to the user's timezone when the chat message is retrieved
//        OffsetDateTime odt = OffsetDateTime.now();
//
//        cm.setTimestamp(odt);
//        cm.setChatRoom(this.getChatRoom(chatRoomId));
//        cm.setUser(this.getUser(userId));
//        cm.setContent(newChatMessage.getContent());
//
//        ChatMessageEntity chatMessageEntity = ChatMessageMapper.INSTANCE.toEntity(cm);
//        chatMessageEntity = chatMessageRepository.save(chatMessageEntity);
//
//        this.updateLastPostedMessageDateTime(userId, odt);
//        return ChatMessageMapper.INSTANCE.toModel(chatMessageEntity);
//    }

//    private ChatRoom getChatRoom(Long chatRoomId) {
//        return chatRoomRepository.findById(chatRoomId).map(ChatRoomMapper.INSTANCE::toModel).orElse(null);
//    }
//
//    private User getUser(Long userId) {
//        return userRepository.findById(userId).map(UserMapper.INSTANCE::toModel).orElse(null);
//    }
//
//    private void updateLastPostedMessageDateTime(Long userId, OffsetDateTime odt) {
//        UserEntity userEntity = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        userEntity.setLastPostedMessageDateTime(odt);
//        userRepository.save(userEntity);
//    }


    public List<ChatMessage> getChatMessagesByChatRoomId(Long chatRoomId) {
        List<ChatMessageEntity> chatMessageEntities = chatMessageRepository.findByChatRoomId(chatRoomId);
        List<ChatMessage> chatMessages = chatMessageEntities.stream().map(ChatMessageMapper.INSTANCE::toModel).collect(Collectors.toList());
        for (ChatMessage chatMessage : chatMessages) {
            chatMessage.setChatRoom(null);
        }

        return chatMessages;
    }
}