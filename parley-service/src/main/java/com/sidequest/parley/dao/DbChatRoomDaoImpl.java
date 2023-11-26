package com.sidequest.parley.dao;

import com.sidequest.parley.exception.ForeignKeyConstraintException;
import org.sidequest.parley.model.ChatRoom;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the ChatRoomDao interface.
 * It is used to get hold information about a chat room.
 * A chat room is a group of users who can chat with each other.
 * It has the following fields:
 * id: the id of the chat room
 * name: the name of the chat room
 * moderatorId: the id of the user who is the moderator of the chat room
 * icon: the icon of the chat room
 * chatMessageId: the id of the chat messages in the chat room
 * userIds: the ids of the users in the chat room
 * *
 */
public class DbChatRoomDaoImpl implements ChatRoomDao {
    Statement statement;
    private final String dbEnv;

    public DbChatRoomDaoImpl(String dbEnv) {
        this.dbEnv = dbEnv;
    }

    @Override
    public boolean isChatRoom(int id) throws SQLException {
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(SchemaChatRoomSql.SELECT_CHAT_ROOM_BY_ID);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new SQLException("Error in isChatRoom method");
        } finally {
            dbConnection.closeConnection();
        }
    }

    @Override
    public List<ChatRoom> getAllChatRooms() {
        List<ChatRoom> chatRooms = new ArrayList<>();
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(SchemaChatRoomSql.SELECT_ALL_CHAT_ROOMS)) {

            while (rs.next()) {
                int chatRoomId = rs.getInt("chatRoomId");
                String name = rs.getString("name");
                byte[] icon = rs.getBytes("icon");
                int moderatorId = rs.getInt("moderatorId");

                ChatRoom chatRoom = new ChatRoom();
                chatRoom.setChatRoomId(chatRoomId);
                chatRoom.setName(name);
                chatRoom.setIcon(icon);
                chatRoom.setModeratorId(moderatorId);
                chatRoom.setUserIds(getUserIdsByChatRoomId(chatRoomId));

                chatRooms.add(chatRoom);
                //chatRooms.add(new ChatRoom(chatRoomId, name, moderatorId, icon, getUserIdsByChatRoomId(chatRoomId))); // this is the old code
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection();
        }
        return chatRooms;
    }

    @Override
    public List<Integer> getUserIdsByChatRoomId(int chatRoomId) {
        List<Integer> arrUserIds = new ArrayList<>();
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatRoomUsersSql.SELECT_CHAT_ROOM_USERS_BY_CHAT_ROOM_ID)) {
            statement.setInt(1, chatRoomId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("userId");
                arrUserIds.add(userId);
            }
            return arrUserIds;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection();
        }
        return arrUserIds;
    }

    @Override
    public void addUserToChatRoom(int chatRoomId, int userId) {
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatRoomUsersSql.INSERT_CHAT_ROOM_USERS)) {
            statement.setInt(1, chatRoomId);
            statement.setInt(2, userId);
            statement.executeUpdate();
            System.out.println("INSERT_CHAT_ROOM_USERS is done...");

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        } finally {
            dbConnection.closeConnection();
        }
    }

    @Override
    public void addUsersToChatRoom(int chatRoomId, List<Integer> userIds) {
        System.out.println("INSERT_CHAT_ROOM_USERS is called...");
        System.out.println("userIds Size: " + userIds.size());
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatRoomUsersSql.INSERT_CHAT_ROOM_USERS)) {
            for (int userId : userIds) {
                statement.setInt(1, chatRoomId);
                statement.setInt(2, userId);
                System.out.println(statement);
                statement.executeUpdate();
            }
            System.out.println("INSERT_CHAT_ROOM_USERS is done...");

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        } finally {
            dbConnection.closeConnection();
        }
    }

    @Override
    public void createChatRoom(ChatRoom chatRoom) throws SQLException {
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatRoomSql.INSERT_CHAT_ROOM)
        ) {
            statement.setInt(1, chatRoom.getChatRoomId());
            statement.setString(2, chatRoom.getName());
            statement.setInt(3, chatRoom.getModeratorId());
            statement.setBytes(4, chatRoom.getIcon());
            statement.executeUpdate();
            System.out.println("INSERT_CHAT_ROOM is done...");
            addUsersToChatRoom(chatRoom.getChatRoomId(), chatRoom.getUserIds());
        } catch (SQLException e) {
            System.out.println("Error code: " + e.getErrorCode());
            e.printStackTrace();
            if (e.getErrorCode() == 19) {
                throw new ForeignKeyConstraintException();
            } else {
                throw new SQLException();
            }
        } finally {
            dbConnection.closeConnection();
        }
    }

    /**
     * this method is used to get a chat room
     *
     * @param chatRoom
     */
    @Override
    public void updateChatRoom(ChatRoom chatRoom) {

    }

    /**
     * this method is used to delete a chat room
     *
     * @param chatRoom
     */
    @Override
    public void deleteChatRoom(ChatRoom chatRoom) {
        // this method should delete a chat room from the database
        // it should delete the chat room from the chat room table
        // it should delete the chat messages from the chat message table
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatRoomSql.DELETE_CHAT_ROOM)) {
            statement.setInt(1, chatRoom.getChatRoomId());
            statement.executeUpdate();
            System.out.println("DELETE_CHAT_ROOM is done...");

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        }

        //it should delete messages with the chat room id from the chat message table
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatMessageSql.DELETE_CHAT_MESSAGE)) {
            statement.setInt(1, chatRoom.getChatRoomId());
            statement.executeUpdate();
            System.out.println("DELETE_CHAT_MESSAGE is done...");

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        } finally {
            dbConnection.closeConnection();
        }

    }

    /**
     * this method is used to drop a chat room table
     */
    @Override
    public void dropChatRoomTable() {
        System.out.println("DROP chat room table");
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatRoomSql.DROP_TABLE)) {
            statement.executeUpdate();
            System.out.println("DROP_TABLE is done...");

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        } finally {
            dbConnection.closeConnection();
        }
    }

    /**
     * this method is used to create a chat room table
     */
    @Override
    public void createChatRoomTable() {
        System.out.println("CREATE chat room table");
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatRoomSql.CREATE_TABLE)
        ) {
            System.out.println("CREATE_TABLE: " + SchemaChatRoomSql.CREATE_TABLE);
            statement.executeUpdate();
            System.out.println("CREATE_TABLE is done...");

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        } finally {
            dbConnection.closeConnection();
        }
    }
}
