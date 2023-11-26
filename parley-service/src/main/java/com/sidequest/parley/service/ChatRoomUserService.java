package com.sidequest.parley.service;

import com.sidequest.parley.dao.DbChatRoomUsersImpl;

public class ChatRoomUserService {
    DbChatRoomUsersImpl dao;

    public ChatRoomUserService() {
        this("prod"); // default to prod. This is constructor chaining.
    }

    public ChatRoomUserService(String dbEnv) {
        dao = new DbChatRoomUsersImpl(dbEnv);
    }

    public boolean isUserInChatRoom(int userId, int chatRoomId) {
        return dao.isUserInChatRoom(userId, chatRoomId);
    }

}
