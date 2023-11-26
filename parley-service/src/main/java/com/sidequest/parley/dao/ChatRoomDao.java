package com.sidequest.parley.dao;

import org.sidequest.parley.model.ChatRoom;

import java.sql.SQLException;
import java.util.List;

public interface ChatRoomDao {

    boolean isChatRoom(int id) throws SQLException;

    List<ChatRoom> getAllChatRooms();


    List<Integer> getUserIdsByChatRoomId(int chatRoomId);


    void addUserToChatRoom(int chatRoomId, int userId);

    void addUsersToChatRoom(int chatRoomId, List<Integer> userIds);

    void createChatRoom(ChatRoom chatRoom) throws SQLException;

    void updateChatRoom(ChatRoom chatRoom) throws SQLException;

    void deleteChatRoom(ChatRoom chatRoom);

    void dropChatRoomTable();

    void createChatRoomTable();
}
