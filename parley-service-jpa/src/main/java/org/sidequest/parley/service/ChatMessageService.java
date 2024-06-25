package org.sidequest.parley.service;

import org.sidequest.parley.entity.ChatMessageEntity;
import org.sidequest.parley.entity.ChatRoomEntity;
import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.helpers.TimeHelper;
import org.sidequest.parley.mapper.ChatMessageMapper;
import org.sidequest.parley.model.ChatMessage;
import org.sidequest.parley.model.NewChatMessage;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.ChatMessageRepository;
import org.sidequest.parley.repository.ChatRoomRepository;
import org.sidequest.parley.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {
    private static final Logger log = Logger.getLogger(ChatMessageService.class.getName());
    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    UserService us;

    @Autowired
    ChatRoomService crs;

    @Autowired
    ChatRoomUserService crus;

//    @Value("${user.avatar.directory}")
//    private String uploadDir;

    public ChatMessageService() {
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessageRepository.findAll().stream().map(ChatMessageMapper.INSTANCE::toModel).collect(Collectors.toList());
    }

    public ChatMessage getChatMessage(Long id) {
        ChatMessage chatMessage = chatMessageRepository.findById(id).map(ChatMessageMapper.INSTANCE::toModel).orElse(null);
        log.fine("getChatMessage: " + chatMessage);

        assert chatMessage != null;
        User user = chatMessage.getUser();
        UserEntity goodUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(goodUser.getName());

        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatMessage.getChatRoom().getChatRoomId()).orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        chatRoom.setName(chatRoom.getName());

        // TODO: Fix the mapper so we don't have to do this manually rebuilt of the object
        ArrayList<User> goodChatRoomUsers = new ArrayList<>();
        for (User chatRoomUser : chatMessage.getChatRoom().getUsers()) {
            UserEntity userEntity = userRepository.findById(chatRoomUser.getId()).orElseThrow(() -> new RuntimeException("User not found"));
            goodChatRoomUsers.add(new User().id(userEntity.getId()).name(userEntity.getName()));
        }
        chatMessage.getChatRoom().setUsers(goodChatRoomUsers);
        return chatMessage;
    }

    public ChatMessage createChatMessage(NewChatMessage newChatMessage) throws SQLException {
        log.fine("createChatMessage: " + newChatMessage);
        ChatMessage cm = new ChatMessage();
        Long chatRoomId = newChatMessage.getChatRoomId();
        Long userId = newChatMessage.getUserId();


        // Check if the user is a member of the chat room
        if (!crus.isUserInChatRoom(userId, chatRoomId)) {
            throw new RuntimeException("Access denied: User is not a member of the chat room");
        }
        log.fine("User is a member of the chat room");

        // Retrieve the user's timezone
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        ZoneId userZoneId = ZoneId.of(userEntity.getTimezone());
        OffsetDateTime odt = TimeHelper.getOffsetDateTimeForUserInUtcTime(userZoneId);

        cm.setTimestamp(odt);
        cm.setChatRoom(crs.getChatRoom(chatRoomId));
        cm.setUser(us.getUser(userId));
        cm.setContent(newChatMessage.getContent());

        ChatMessageEntity chatMessageEntity = ChatMessageMapper.INSTANCE.toEntity(cm);
        chatMessageEntity = chatMessageRepository.save(chatMessageEntity);

        this.updateLastPostedMessageDateTime(userId, odt);

        return ChatMessageMapper.INSTANCE.toModel(chatMessageEntity);
    }

    private Instant toUtc(ZonedDateTime localDateTime) {
        return localDateTime.toInstant();
    }

    private void updateLastPostedMessageDateTime(Long userId, OffsetDateTime odt) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userEntity.setLastPostedMessageDateTime(odt);
        userRepository.save(userEntity);
    }

    public List<ChatMessage> getChatMessagesByChatRoomId(Long chatRoomId) {
        List<ChatMessageEntity> chatMessageEntities = chatMessageRepository.findByChatRoomId(chatRoomId);
        List<ChatMessage> chatMessages = chatMessageEntities.stream().map(ChatMessageMapper.INSTANCE::toModel).collect(Collectors.toList());
        for (ChatMessage chatMessage : chatMessages) {
            chatMessage.setChatRoom(null);
        }

        return chatMessages;
    }
}