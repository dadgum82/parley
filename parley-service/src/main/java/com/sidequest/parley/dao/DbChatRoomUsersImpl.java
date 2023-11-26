package com.sidequest.parley.dao;

import com.sidequest.parley.model.ChatRoomUsers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DbChatRoomUsersImpl implements ChatRoomUsersDao {
    // private SQLiteConnection dbConnection;
    private final String dbEnv;

    public DbChatRoomUsersImpl(String dbEnv) {
        this.dbEnv = dbEnv;
    }

    @Override
    public List<ChatRoomUsers> getAllChatRoomUsers(int chatRoomId) {
        return null;
    }

    @Override
    public void createChatRoomUsers(ChatRoomUsers chatRoomUsers) {

    }

    @Override
    public void updateChatRoomUsers(ChatRoomUsers chatRoomUsers) {

    }

    @Override
    public void deleteChatRoomUsers(ChatRoomUsers chatRoomUsers) {

    }

    /**
     * This method drops the chat_room_users table.
     */
    @Override
    public void dropChatRoomUsersTable() {
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatRoomUsersSql.DROP_TABLE)
        ) {
            //statement.execute(SchemaChatRoomUsersSql.DROP_TABLE);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection();
        }
    }

    /**
     * This method creates the chat_room_users table.
     * The table has 2 columns: chat_room_id and user_id.
     * The chatRoomId column is a foreign key to the chat_room table.
     * The userId column is a foreign key to the users table.
     */
    @Override
    public void createChatRoomUsersTable() {
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatRoomUsersSql.CREATE_TABLE)
        ) {
            System.out.println("CREATE_TABLE: " + SchemaChatRoomUsersSql.CREATE_TABLE);
            // statement.execute(SchemaChatRoomUsersSql.CREATE_TABLE);
            statement.executeUpdate();
            System.out.println("CREATE_TABLE is done...");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection();
        }
    }

    public boolean isUserInChatRoom(int userId, int chatRoomId) {
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatRoomUsersSql.SELECT_USER_IN_CHAT_ROOM)
        ) {
            statement.setInt(1, userId);
            statement.setInt(2, chatRoomId);
            System.out.println("SELECT_USER_IN_CHAT_ROOM: " + SchemaChatRoomUsersSql.SELECT_USER_IN_CHAT_ROOM);
            System.out.println("userId: " + userId + " chatRoomId: " + chatRoomId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                System.out.println("+++user is in chat room");
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection();
        }
        return false;
    }
}
