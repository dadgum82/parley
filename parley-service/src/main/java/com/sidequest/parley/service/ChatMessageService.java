package com.sidequest.parley.service;

import com.sidequest.parley.dao.DbChatMessageDaoImpl;
import org.sidequest.parley.model.ChatMessage;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.NewChatMessage;
import org.sidequest.parley.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatMessageService {
    private int CHAT_MESSAGE_COUNT;
    private final List<ChatMessage> chatMessages;

    DbChatMessageDaoImpl dao;
    UserService us;
    ChatRoomService crs;

// --Commented out by Inspection START (11/26/2023 8:06 AM):
//    public ChatMessageService() throws IOException, SQLException {
//        this("prod"); // default to prod. This is constructor chaining.
//    }
// --Commented out by Inspection STOP (11/26/2023 8:06 AM)

    public ChatMessageService(String dbEnv) throws IOException, SQLException {
        this(dbEnv, 0);
    }

    public ChatMessageService(int chatRoomId) throws IOException, SQLException {
        this("prod", chatRoomId);
    }

    public ChatMessageService(String dbEnv, int chatRoomId) throws IOException, SQLException {
        this.CHAT_MESSAGE_COUNT = 0;
        this.chatMessages = new ArrayList<>();
        dao = new DbChatMessageDaoImpl(dbEnv);
        crs = new ChatRoomService(dbEnv);
        us = new UserService(dbEnv);
        this.initalizeChat(chatRoomId);
    }

    private void initalizeChat(int chatRoomId) throws SQLException {
        chatMessages.addAll(dao.getAllChatMessages(chatRoomId));
        this.CHAT_MESSAGE_COUNT = chatMessages.size();
    }

    public void createChatMessage(NewChatMessage newChatMessage) throws SQLException {
        int messageID = this.CHAT_MESSAGE_COUNT;
        messageID++;

        System.out.println("chatMessageInput.getUserId(): " + newChatMessage.getUserId());
        User user = newChatMessage.getUserId() == null ? null : us.getUser(newChatMessage.getUserId());
        ChatRoom chatRoom = crs.getChatRoom(newChatMessage.getChatRoomId());
        if (chatRoom == null) {
            throw new IllegalArgumentException("ChatRoom not found. Chat Message: " + newChatMessage);
        }

        if (user == null) {
            throw new IllegalArgumentException("User not found. Chat Message: " + newChatMessage);
        }

        System.out.println("The user name is: " + user.getName());
        System.out.println("The chat room name is: " + chatRoom.getName());
        ChatRoomUserService crus = new ChatRoomUserService();
        if (!crus.isUserInChatRoom(user.getId(), chatRoom.getChatRoomId())) {
            throw new IllegalArgumentException("User is not in chat room. Username: " + user.getName() + " ChatRoom name: " + chatRoom.getName());
        }

        String content = newChatMessage.getContent();
        LocalDateTime currentTime = LocalDateTime.now();
        //int id, int chatRoomId, LocalDateTime timestamp, User sender, String content
        // ChatMessage cm = new ChatMessage(messageID, chatMessageInput.getChatRoomId(), currentTime, user, content); // this is the old code
        ChatMessage cm = new ChatMessage();
        cm.setId(messageID);
        cm.setChatRoomId(newChatMessage.getChatRoomId());
        cm.setTimestamp(OffsetDateTime.from(currentTime));
        cm.setUser(user);
        cm.setContent(content);

        System.out.println("createChatMessage: cm.id: " + cm.getId());
        System.out.println("createChatMessage: cm.chatRoomId: " + cm.getChatRoomId());
        System.out.println("createChatMessage: cm.user.id: " + cm.getUser().getId());
        System.out.println("createChatMessage: cm.timestamp: " + cm.getTimestamp());
        System.out.println("createChatMessage: cm.content: " + cm.getContent());

        dao.createChatMessage(cm);
        this.chatMessages.add(cm);
        this.CHAT_MESSAGE_COUNT = messageID;
        // return cm;
    }

    public List<ChatMessage> getChatMessages() {
        return this.chatMessages;
    }

    public List<ChatMessage> getChatMessages(int chatID) {
        List<ChatMessage> result = new ArrayList<>();
        for (ChatMessage cm : this.chatMessages) {
            if (cm.getId() == chatID) {
                result.add(cm);
            }
        }
        return result;
    }


}