package com.sidequest.parley.dao;

import org.sidequest.parley.model.ChatMessage;

import java.sql.SQLException;
import java.util.List;

public interface ChatMessageDao {
    List<ChatMessage> getAllChatMessages(int chatRoomId) throws SQLException;

    void createChatMessage(ChatMessage chatMessage) throws SQLException;

    void updateChatMessage(ChatMessage chatMessage) throws SQLException;

    void deleteChatMessage(ChatMessage chatMessage) throws SQLException;

    void dropChatMessageTable() throws SQLException;

    void createChatMessageTable() throws SQLException;
}
