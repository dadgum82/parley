package com.sidequest.parley.dao;

import com.sidequest.parley.model.ChatRoomUsers;

import java.util.List;

public interface ChatRoomUsersDao {

    List<ChatRoomUsers> getAllChatRoomUsers(int chatRoomId);

    void createChatRoomUsers(ChatRoomUsers chatRoomUsers);

    void updateChatRoomUsers(ChatRoomUsers chatRoomUsers);

    void deleteChatRoomUsers(ChatRoomUsers chatRoomUsers);

    void dropChatRoomUsersTable();

    void createChatRoomUsersTable();
}
