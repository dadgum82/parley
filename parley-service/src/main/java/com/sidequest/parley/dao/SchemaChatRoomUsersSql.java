package com.sidequest.parley.dao;

/**
 * This class is used to generate the database schema for the chat_room_users table.
 * The table has 2 columns: chat_room_id and user_id.
 * The chatRoomId column is a foreign key to the chat_room table.
 * The userId column is a foreign key to the users table.
 * The chat_room_users table is used to keep track of which users are in which chat rooms.
 * This table is used to determine which users should receive a chat message.
 * This table is also used to determine which chat rooms a user is in.
 */
public class SchemaChatRoomUsersSql {
    private SchemaChatRoomUsersSql() {
    }

    public static final String INSERT_CHAT_ROOM_USERS = "INSERT INTO chat_room_users (chatRoomId, userId) VALUES (?, ?)";
    public static final String SELECT_USER_IN_CHAT_ROOM = "SELECT * FROM chat_room_users WHERE userId = ? AND chatRoomId = ?";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS chat_room_users (userId INTEGER, chatRoomId INTEGER, FOREIGN KEY (userId) REFERENCES user(userId), FOREIGN KEY (chatRoomId) REFERENCES chat_room(chatRoomId))";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS chat_room_users";
    public static final String SELECT_CHAT_ROOM_USERS_BY_CHAT_ROOM_ID = "SELECT * FROM chat_room_users WHERE chatRoomId = ?";

}
