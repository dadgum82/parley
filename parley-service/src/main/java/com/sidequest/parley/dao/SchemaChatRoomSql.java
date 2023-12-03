package com.sidequest.parley.dao;

/**
 * This class is used to create the database schema for the ChatRoom table.
 * A chat room is a group of users who can chat with each other.
 * It has the following fields:
 * <p>
 * id: the id of the chat room
 * name: the name of the chat room
 * moderatorId: the id of the user who is the moderator of the chat room
 * icon: the icon of the chat room
 * chatMessageId: the id of the chat messages in the chat room
 * userIds: the ids of the users in the chat room
 */
public class SchemaChatRoomSql {
    public static final String INSERT_CHAT_ROOM = "INSERT INTO chat_room (chatRoomId, name, moderatorId, icon) VALUES (?, ?, ?, ?)";

    private SchemaChatRoomSql() {
    }

    public static final String UPDATE_CHAT_ROOM = "UPDATE chat_room SET name = ?, moderatorId = ?, icon = ? WHERE chatRoomId = ?";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS chat_room (chatRoomId INTEGER PRIMARY KEY, name TEXT NOT NULL, moderatorId INTEGER NOT NULL, icon BLOB, FOREIGN KEY (moderatorId) REFERENCES user(userId))";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS chat_room";

    public static final String DELETE_CHAT_ROOM = "DELETE FROM chat_room WHERE chatRoomId = ?";
    public static final String SELECT_ALL_CHAT_ROOMS = "SELECT * FROM chat_room";
    public static final String SELECT_CHAT_ROOM_BY_ID = "SELECT * FROM chat_room WHERE chatRoomId = ?";
    // --Commented out by Inspection (11/26/2023 8:06 AM):public static final String SELECT_CHAT_ROOM_BY_NAME = "SELECT * FROM chat_room WHERE name = ?";
    // --Commented out by Inspection (11/26/2023 8:06 AM):public static final String SELECT_CHAT_ROOM_BY_MODERATOR_ID = "SELECT * FROM chat_room WHERE moderatorId = ?";


}
