package com.sidequest.parley.dao;

/**
 * This class is used to create the database schema for the ChatMessage table.
 * A chat message is a message sent by a user in a chat room.
 * It has the following fields:
 * <p>
 * id: the id of the chat message
 * userId: the id of the user who sent the chat message
 * content: the content of the chat message
 * timestamp: the timestamp of the chat message
 */
public class SchemaChatMessageSql {
    //public static final String INSERT_CHAT_MESSAGE = "INSERT INTO chat_message (id, chatRoomId, userId, content, timestamp) VALUES (?, ?, ?, ?, ?)";
    public static final String INSERT_CHAT_MESSAGE = "INSERT INTO chat_message (chatRoomId, userId, content, timestamp) VALUES (?, ?, ?, ?)";
    //public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS chat_message (id INTEGER NOT NULL, chatRoomId INTEGER,userId INTEGER, content TEXT NOT NULL,   timestamp TEXT NOT NULL,   FOREIGN KEY (userId) REFERENCES user(userId),   FOREIGN KEY (chatRoomId) REFERENCES chat_room(chatRoomId))";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS chat_message (id INTEGER PRIMARY KEY AUTOINCREMENT, chatRoomId INTEGER,userId INTEGER, content TEXT NOT NULL,   timestamp TEXT NOT NULL,   FOREIGN KEY (userId) REFERENCES user(userId),   FOREIGN KEY (chatRoomId) REFERENCES chat_room(chatRoomId))";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS chat_message";
    public static final String DELETE_CHAT_MESSAGE = "DELETE FROM chat_message WHERE id = ?";

    public static final String DELETE_CHAT_MESSAGE_BY_CHAT_ROOM_ID = "DELETE FROM chat_message WHERE chatRoomId = ?";

    // --Commented out by Inspection (11/26/2023 8:06 AM):public static final String DELETE_CHAT_MESSAGE_BY_CHAT_ROOM_ID = "DELETE FROM chat_message WHERE chatRoomId = ?";
    public static final String SELECT_ALL_CHAT_MESSAGES_BY_CHAT_ROOM_ID = "SELECT * FROM chat_message WHERE chatRoomId = ? ORDER BY id ASC";
    public static final String UPDATE_CHAT_MESSAGE = "UPDATE chat_message SET content = ? WHERE id = ?";

    private SchemaChatMessageSql() {
    }
}
