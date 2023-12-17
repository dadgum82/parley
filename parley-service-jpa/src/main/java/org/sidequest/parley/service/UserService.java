package org.sidequest.parley.service;

import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final List<User> users;
    private int USERS_COUNT;
    UserRepository repository;

    public UserService() throws IOException {
        this("prod");

    }

    public UserService(String dbEnv) throws IOException {
        users = new ArrayList<>();
        this.initalizeUsers();
    }

    private void initalizeUsers() {
        log.info("-------------------------------");
        repository.findAll().forEach(user -> {
            this.users.add(user);
            log.info(user.toString());
        });

        this.USERS_COUNT = this.users.size();
    }

    public List<User> getUsers() {
        return this.users;
    }

    public User getUser(int userId) {
        for (User u : this.users) {
            if (u.getId() == userId) {
                return u;
            }
        }
        return null;
    }

// --Commented out by Inspection START (11/26/2023 8:06 AM):
//    public User getUser(String name) {
//        for (User u : this.users) {
//            if (u.getName().equalsIgnoreCase(name)) {
// --Commented out by Inspection START (11/26/2023 8:06 AM):
////                return u;
////            }
////        }
////        return null;
// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
//    }
// --Commented out by Inspection STOP (11/26/2023 8:06 AM)

    public int getUserCount() {
        return USERS_COUNT;
    }

    public User createUser(String name) {
        int userCount = this.USERS_COUNT;
        userCount++; //new user increment count
        //User user = new User(userCount, name); // This is the original code. It is commented out because it is not compatible with the new User class.
        User user = new User();
        user.setId(userCount);
        user.setName(name);
        try {
            repository.save(user);
            System.out.println("User created");
            this.users.add(user);
            this.USERS_COUNT = userCount;
            return user;

        } catch (Exception e) {
            System.out.println("User not created");
        }
        return null;
    }

    public void updateUser(String name, int id) {
        // User user = new User(id, name); // This is the original code. It is commented out because it is not compatible with the new User class.
        User user = new User();
        user.setId(id);
        user.setName(name);

        try {
            repository.save(user);
            System.out.println("User updated");
        } catch (Exception e) {
            System.out.println("User not updated");
        }
    }
}
