package com.sidequest.parley.util;

import com.sidequest.parley.dao.DbChatMessageDaoImpl;
import com.sidequest.parley.dao.DbChatRoomDaoImpl;
import com.sidequest.parley.dao.DbChatRoomUsersImpl;
import com.sidequest.parley.dao.DbUserDaoImpl;

import java.sql.SQLException;

/**
 * This class is used to generate the database schema for the ChatMessage, ChatRoom, and User tables.
 */
public class GenerateDBschema {

    private GenerateDBschema(String dbEnv) throws SQLException {
        if (dbEnv == null || dbEnv.isEmpty()) {
            throw new IllegalArgumentException("dbEnv cannot be null or empty");
        }
        if (dbEnv.equals("prod") || dbEnv.equals("test")) {
            System.out.println("dbEnv is valid");
            buildDatabase(dbEnv);
        } else {
            throw new IllegalArgumentException("dbEnv must be either prod or test");
        }
    }

    private static void buildDatabase(String dbEnv) throws SQLException {
        DbUserDaoImpl dbUserDao = new DbUserDaoImpl(dbEnv);
        DbChatMessageDaoImpl dbChatMessageDao = new DbChatMessageDaoImpl(dbEnv);
        DbChatRoomDaoImpl dbChatRoomDao = new DbChatRoomDaoImpl(dbEnv);
        DbChatRoomUsersImpl dbChatRoomUsers = new DbChatRoomUsersImpl(dbEnv);

        //We are creating the tables in the following order:
        dbChatRoomDao.createChatRoomTable();
        dbUserDao.createUserTable();
        dbChatRoomUsers.createChatRoomUsersTable();
        dbChatMessageDao.createChatMessageTable();
    }

    public static void main(String[] args) throws SQLException {
        String dbEnv = "prod";
        //I need to check that I have 1 argument or if the argument is null or empty to set the dbEnv to prod
        if (args.length != 1 || args[0] == null || args[0].isEmpty()) {
            System.out.println("Usage: java GenerateDBschema <dbName>");
            System.out.println("dbName is either prod or test. We are setting it to prod for you.");
        } else {
            dbEnv = args[0];
        }

        buildDatabase(dbEnv);
    }
}
