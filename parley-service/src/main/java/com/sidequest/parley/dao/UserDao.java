package com.sidequest.parley.dao;

import org.sidequest.parley.model.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    User getUserById(int id);

    User getUserByName(String name);


    void createUser(User user);

    void updateUser(User user);


    void deleteUser(User user);

    void dropUserTable();

    void createUserTable();
}
