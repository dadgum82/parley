package org.sidequest.parley.service;

import org.sidequest.parley.entity.ChatMessageEntity;
import org.sidequest.parley.mapper.ChatMessageMapper;
import org.sidequest.parley.model.ChatMessage;
import org.sidequest.parley.model.NewChatMessage;
import org.sidequest.parley.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    private ChatMessageRepository chatMessageRepository;
    private UserService userService;
    private ChatRoomService chatRoomService;

    @Autowired
    public void setChatMessageRepository(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    //  @Autowired
    public void setUserService(@Lazy UserService userService) {
        this.userService = userService;
    }

    // @Autowired
    public void setChatRoomService(@Lazy ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
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



    public List<ChatMessage> getChatMessagesByChatRoomId(Long chatRoomId) {
        List<ChatMessageEntity> chatMessageEntities = chatMessageRepository.findByChatRoomId(chatRoomId);
        List<ChatMessage> chatMessages = chatMessageEntities.stream().map(ChatMessageMapper.INSTANCE::toModel).collect(Collectors.toList());
        for (ChatMessage chatMessage : chatMessages) {
            chatMessage.setChatRoom(null);
        }

        return chatMessages;
    }
}