package org.sidequest.parley.service;

import org.sidequest.parley.entity.ChatMessageEntity;
import org.sidequest.parley.mapper.ChatMessageMapper;
import org.sidequest.parley.model.ChatMessage;
import org.sidequest.parley.model.NewChatMessage;
import org.sidequest.parley.repository.ChatMessageRepository;
import org.sidequest.parley.repository.ChatRoomRepository;
import org.sidequest.parley.repository.ChatRoomUsersRepository;
import org.sidequest.parley.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {

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

    public List<ChatMessage> getChatMessages(int chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId).stream()
                .map(ChatMessageMapper.INSTANCE::toModel)
                .collect(Collectors.toList());
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
}


//////////////////////////////////////////

//@Service
//public class ChatMessageService {
//    private List<ChatMessage> chatMessages;
//    @Autowired
//    ChatMessageRepository chatMessageRepository;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    ChatRoomRepository chatRoomRepository;
//    @Autowired
//    ChatRoomUsersRepository chatRoomUsersRepository;
//
//    @Autowired
//    UserService us;
//
//    @Autowired
//    ChatRoomService crs;
//
//    @Autowired
//    ChatRoomUserService crus;
//    private int CHAT_MESSAGE_COUNT;
//
//    public ChatMessageService() {
//
//    }
//
//    public void initalizeChat(int chatRoomId) throws SQLException {
//        this.CHAT_MESSAGE_COUNT = 0;
//        this.chatMessages = new ArrayList<>();
//
//        chatMessageRepository.findAll().forEach(chatMessageEntity -> {
//            ChatMessage chatMessage = ChatMessageMapper.INSTANCE.mapTo(chatMessageEntity);
//            this.chatMessages.add(chatMessage);
//
//        });
//
//        this.CHAT_MESSAGE_COUNT = chatMessages.size();
//    }
//
//    public void createChatMessage(NewChatMessage newChatMessage) throws SQLException {
//        int messageID = this.CHAT_MESSAGE_COUNT;
//        messageID++;
//
//        System.out.println("chatMessageInput.getUserId(): " + newChatMessage.getUserId());
//        User user = newChatMessage.getUserId() == null ? null : us.getUser(newChatMessage.getUserId());
//        ChatRoom chatRoom = crs.getChatRoom(newChatMessage.getChatRoomId());
//        if (chatRoom == null) {
//            throw new IllegalArgumentException("ChatRoom not found. Chat Message: " + newChatMessage);
//        }
//
//        if (user == null) {
//            throw new IllegalArgumentException("User not found. Chat Message: " + newChatMessage);
//        }
//
//        System.out.println("The user name is: " + user.getName());
//        System.out.println("The chat room name is: " + chatRoom.getName());
//
//        if (!crus.isUserInChatRoom(user.getId(), chatRoom.getChatRoomId())) {
//            throw new IllegalArgumentException("User is not in chat room. Username: " + user.getName() + " ChatRoom name: " + chatRoom.getName());
//        }
//
//        String content = newChatMessage.getContent();
//        LocalDateTime currentTime = LocalDateTime.now();
//        //int id, int chatRoomId, LocalDateTime timestamp, User sender, String content
//        // ChatMessage cm = new ChatMessage(messageID, chatMessageInput.getChatRoomId(), currentTime, user, content); // this is the old code
//        ChatMessage cm = new ChatMessage();
//        cm.setId(messageID);
//        cm.setChatRoomId(newChatMessage.getChatRoomId());
//        cm.setTimestamp(OffsetDateTime.from(currentTime));
//        cm.setUser(user);
//        cm.setContent(content);
//
//        System.out.println("createChatMessage: cm.id: " + cm.getId());
//        System.out.println("createChatMessage: cm.chatRoomId: " + cm.getChatRoomId());
//        System.out.println("createChatMessage: cm.user.id: " + cm.getUser().getId());
//        System.out.println("createChatMessage: cm.timestamp: " + cm.getTimestamp());
//        System.out.println("createChatMessage: cm.content: " + cm.getContent());
//
//        ChatMessageEntity chatMessageEntity = ChatMessageMapper.INSTANCE.mapTo(cm);
//        chatMessageRepository.save(chatMessageEntity);
//
//        this.chatMessages.add(cm);
//        this.CHAT_MESSAGE_COUNT = messageID;
//        // return cm;
//    }
//
//    public List<ChatMessage> getChatMessages() {
//        return this.chatMessages;
//    }
//
//    public List<ChatMessage> getChatMessages(int chatID) {
//        List<ChatMessage> result = new ArrayList<>();
//        for (ChatMessage cm : this.chatMessages) {
//            if (cm.getId() == chatID) {
//                result.add(cm);
//            }
//        }
//        return result;
//    }
//
//
//}