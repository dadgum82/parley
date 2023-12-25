package org.sidequest.parley.service;

import org.sidequest.parley.entity.ChatMessageEntity;
import org.sidequest.parley.entity.ChatRoomEntity;
import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.mapper.ChatMessageMapper;
import org.sidequest.parley.model.ChatMessage;
import org.sidequest.parley.model.NewChatMessage;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.ChatMessageRepository;
import org.sidequest.parley.repository.ChatRoomRepository;
import org.sidequest.parley.repository.ChatRoomUsersRepository;
import org.sidequest.parley.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {
    private static final Logger log = LoggerFactory.getLogger(ChatMessageService.class);
    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;
    @Autowired
    ChatRoomUsersRepository chatRoomUsersRepository;

    @Autowired
    UserService us;

    @Autowired
    ChatRoomService crs;

    @Autowired
    ChatRoomUserService crus;

    @Value("${user.avatar.directory}")
    private String uploadDir;

    public ChatMessageService() {
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessageRepository.findAll().stream()
                .map(ChatMessageMapper.INSTANCE::toModel)
                .collect(Collectors.toList());
    }

    public ChatMessage getChatMessage(Long id) {
        ChatMessage chatMessage = chatMessageRepository.findById(id)
                .map(ChatMessageMapper.INSTANCE::toModel).orElse(null);
        log.debug("getChatMessage: {}", chatMessage);

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

    public void createChatMessage(NewChatMessage newChatMessage) throws SQLException {
        ChatMessage cm = new ChatMessage();
        cm.setChatRoom(newChatMessage.getChatRoom());
        cm.setTimestamp(OffsetDateTime.now());
        cm.setUser(newChatMessage.getUser());
        cm.setContent(newChatMessage.getContent());

        ChatMessageEntity chatMessageEntity = ChatMessageMapper.INSTANCE.toEntity(cm);
        chatMessageRepository.save(chatMessageEntity);
    }

    public List<ChatMessage> getChatMessagesByChatRoomId(Long chatRoomId) {
        List<ChatMessageEntity> chatMessageEntities = chatMessageRepository.findByChatRoomId(chatRoomId);
        List<ChatMessage> chatMessages = chatMessageEntities.stream()
                .map(ChatMessageMapper.INSTANCE::toModel)
                .collect(Collectors.toList());
        for (ChatMessage chatMessage : chatMessages) {
            chatMessage.setChatRoom(null);
        }

        return chatMessages;
    }

}