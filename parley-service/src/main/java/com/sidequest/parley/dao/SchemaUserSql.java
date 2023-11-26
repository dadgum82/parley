package com.sidequest.parley.dao;

/**
 * This class is used to create the database schema for the User table.
 * A user is a person who can chat in a chat room.
 * It has the following fields:
 * id: the id of the user
 * name: the name of the user
 */
public final class SchemaUserSql {
    public static final String UPDATE_USER = "UPDATE user SET name = ? WHERE userId = ?";

    private SchemaUserSql() {
    }

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS user (userId INTEGER PRIMARY KEY, name TEXT NOT NULL)";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS user";
    public static final String INSERT_USER = "INSERT INTO user (userId, name) VALUES (?,?)";
    public static final String SELECT_USER_BY_ID = "SELECT * FROM user WHERE userId = ?";
    public static final String SELECT_USER_BY_NAME = "SELECT * FROM user WHERE name = ?";
    public static final String DELETE_USER = "DELETE FROM user WHERE userId = ?";
    public static final String SELECT_ALL_USERS = "SELECT * FROM user";

}
