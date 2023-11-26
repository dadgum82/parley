package com.sidequest.parley.dao;

import org.sidequest.parley.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to access the User table in the database.
 */
public class DbUserDaoImpl implements UserDao {
    //private SQLiteConnection dbConnection;
    private final String dbEnv;

    /**
     * Constructor for DbUserDaoImpl.
     *
     * @param dbEnv The database environment to use. Valid values are "prod" and "test".
     */
    public DbUserDaoImpl(String dbEnv) {
        this.dbEnv = dbEnv;
        // dbConnection = new SQLiteConnection(dbEnv);
    }

    /**
     * get all users
     *
     * @return List<User> users
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SchemaUserSql.SELECT_ALL_USERS)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("userId");
                String name = resultSet.getString("name");
                System.out.println("---id: " + id + " name: " + name);
                // users.add (new User(id, name)); // This is the original code. It is commented out because it is not compatible with the new User class.
                User user = new User();
                user.setId(id);
                user.setName(name);
                users.add(user);
            }

            for (User user : users) {
                System.out.println("+++user: " + user.getId() + " " + user.getName());
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        } finally {
            dbConnection.closeConnection();
        }

        return users;
    }

    /**
     * get user by id
     *
     * @param id
     * @return User user
     */
    @Override
    public User getUserById(int id) {
        User user = null;
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaUserSql.SELECT_USER_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");


                // user = new User(id, name); // This is the original code. It is commented out because it is not compatible with the new User class.
                user = new User();
                user.setId(id);
                user.setName(name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        } finally {
            dbConnection.closeConnection();
        }

        return user;
    }

    /**
     * get user by name
     *
     * @param name
     * @return
     */
    @Override
    public User getUserByName(String name) {
        User user = null;
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaUserSql.SELECT_USER_BY_NAME)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int rsId = resultSet.getInt("id");
                String rsName = resultSet.getString("name");
                // user = new User(rsId, rsName); // This is the original code. It is commented out because it is not compatible with the new User class.
                user = new User();
                user.setId(rsId);
                user.setName(rsName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        } finally {
            dbConnection.closeConnection();
        }
        return user;
    }


    /**
     * create user
     *
     * @param user
     */
    @Override
    public void createUser(User user) {
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(SchemaUserSql.INSERT_USER)) {
            statement.setInt(1, user.getId());
            statement.setString(2, user.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        } finally {
            dbConnection.closeConnection();
        }
    }

    /**
     * update user
     *
     * @param user
     */
    @Override
    public void updateUser(User user) {
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaUserSql.UPDATE_USER)) {

            statement.setString(1, user.getName());
            statement.setInt(2, user.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        } finally {
            dbConnection.closeConnection();
        }

    }

    /**
     * delete user
     *
     * @param user
     */
    @Override
    public void deleteUser(User user) {
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaUserSql.DELETE_USER)) {

            statement.setInt(1, user.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error
        } finally {
            dbConnection.closeConnection();
        }
    }

    /**
     * drop user table
     */
    @Override
    public void dropUserTable() {
        System.out.println("DROP user table");
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaUserSql.DROP_TABLE)) {
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
     * create user table
     */
    @Override
    public void createUserTable() {
        System.out.println("CREATE user table");
        SQLiteConnection dbConnection = new SQLiteConnection(dbEnv);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SchemaUserSql.CREATE_TABLE)) {

            System.out.println("CREATE_TABLE: " + SchemaUserSql.CREATE_TABLE);
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
