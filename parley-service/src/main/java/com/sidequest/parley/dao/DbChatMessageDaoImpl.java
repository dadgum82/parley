package com.sidequest.parley.dao;

import com.sidequest.parley.exception.ForeignKeyConstraintException;
import org.sidequest.parley.model.ChatMessage;
import org.sidequest.parley.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the implementation of the ChatMessageDao interface.
 * It is responsible for handling the database interactions for the ChatMessage model.
 *
 * @see com.sidequest.parley.model.ChatMessage
 * @see com.sidequest.parley.dao.ChatMessageDao
 */
public class DbChatMessageDaoImpl implements ChatMessageDao {
    // private SQLiteConnection dbConnection;
    String dbEnv;

    public DbChatMessageDaoImpl(String dbEnv) {
        this.dbEnv = dbEnv;
        //dbConnection = new SQLiteConnection(dbEnv);
    }

    @Override
    public List<ChatMessage> getAllChatMessages(int chatRoomId) throws SQLException {
        System.out.println("getAllChatMessages starting");
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        List<ChatMessage> chatMessages = new ArrayList<>();
        DbUserDaoImpl userDao = new DbUserDaoImpl(dbEnv);
        DbChatRoomDaoImpl chatRoomDao = new DbChatRoomDaoImpl(dbEnv);
        if (!chatRoomDao.isChatRoom(chatRoomId)) {
            throw new ForeignKeyConstraintException("Chat room does not exist");
        }

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatMessageSql.SELECT_ALL_CHAT_MESSAGES_BY_CHAT_ROOM_ID)) {
            statement.setInt(1, chatRoomId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet == null) {
                System.out.println("resultSet is null");
            }

            while (resultSet.next()) {
                int rsID = resultSet.getInt("id");
                int rsChatRoomId = resultSet.getInt("chatRoomId");
                String rsContent = resultSet.getString("content");
                int rsUserId = resultSet.getInt("userId");
                String rsTimestamp = resultSet.getString("timestamp");
                System.out.println("---id: " + rsID + " userId: " + rsUserId + " content: " + rsContent + " timestamp: " + rsTimestamp);
                LocalDateTime ldt = LocalDateTime.parse(rsTimestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                User user = userDao.getUserById(rsUserId);
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(rsID);
                chatMessage.setChatRoomId(rsChatRoomId);
                chatMessage.setContent(rsContent);
                chatMessage.setTimestamp(ldt.atOffset(java.time.ZoneOffset.UTC));
                chatMessage.setUser(user);
                //int id, int chatRoomId, LocalDateTime timestamp, User sender, String content
                //chatMessages.add(new ChatMessage(rsID, rsChatRoomId, ldt, user, rsContent)); // this is the old code
                chatMessages.add(chatMessage);
            }
            return chatMessages;
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            throw new SQLException("Chat messages could not be retrieved");
        } finally {
            dbConnection.closeConnection();
        }
    }

    @Override
    public void createChatMessage(ChatMessage chatMessage) throws SQLException {
        System.out.println("CREATE chat message");
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatMessageSql.INSERT_CHAT_MESSAGE)) {
            //"INSERT INTO chat_message (chatRoomId, senderId, content, timestamp) VALUES (?, ?, ?, ?)";
            //statement.setInt(1, chatMessage.getId());
            statement.setInt(1, chatMessage.getChatRoomId());
            statement.setInt(2, chatMessage.getUser().getId());
            statement.setString(3, chatMessage.getContent());
            statement.setString(4, chatMessage.getTimestamp().toString());
            System.out.println("INSERT_CHAT_MESSAGE: " + statement);
            statement.executeUpdate();
            System.out.println("INSERT_CHAT_MESSAGE is done...");
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            // Handle database access error
            throw new SQLException("Chat message could not be created");
        } finally {
            dbConnection.closeConnection();
        }

    }

    @Override
    public void updateChatMessage(ChatMessage chatMessage) throws SQLException {
        System.out.println("UPDATE chat message");
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatMessageSql.UPDATE_CHAT_MESSAGE)) {

            statement.setInt(1, chatMessage.getId());
            statement.setString(2, chatMessage.getContent());
            statement.setString(3, chatMessage.getTimestamp().toString());
            System.out.println("UPDATE_CHAT_MESSAGE: " + SchemaChatMessageSql.UPDATE_CHAT_MESSAGE);
            statement.executeUpdate();
            System.out.println("UPDATE_CHAT_MESSAGE is done...");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
            throw new SQLException("Chat message could not be updated");
        } finally {
            dbConnection.closeConnection();
        }
    }

    @Override
    public void deleteChatMessage(ChatMessage chatMessage) throws SQLException {
        System.out.println("DELETE chat message");
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatMessageSql.DELETE_CHAT_MESSAGE)) {

            statement.setInt(1, chatMessage.getId());
            System.out.println("DELETE_CHAT_MESSAGE: " + SchemaChatMessageSql.DELETE_CHAT_MESSAGE);
            statement.executeUpdate();
            System.out.println("DELETE_CHAT_MESSAGE is done...");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
            throw new SQLException("Chat message could not be deleted");
        } finally {
            dbConnection.closeConnection();
        }
    }

    @Override
    public void dropChatMessageTable() throws SQLException {
        System.out.println("DROP chat_message table");
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatMessageSql.DROP_TABLE)) {
            statement.executeUpdate();
            System.out.println("DROP_TABLE is done...");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
            throw new SQLException("Chat message table could not be dropped");
        } finally {
            dbConnection.closeConnection();
        }
    }

    @Override
    public void createChatMessageTable() throws SQLException {
        System.out.println("CREATE chat message table");
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaChatMessageSql.CREATE_TABLE)) {
            System.out.println("CREATE_TABLE: " + SchemaChatMessageSql.CREATE_TABLE);
            statement.executeUpdate();
            System.out.println("CREATE_TABLE is done...");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
            throw new SQLException("Chat message table could not be created");
        } finally {
            dbConnection.closeConnection();
        }
    }

}
