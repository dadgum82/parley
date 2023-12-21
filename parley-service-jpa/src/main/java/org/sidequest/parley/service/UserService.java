package org.sidequest.parley.service;

import jakarta.annotation.PostConstruct;
import org.sidequest.parley.mapper.UserMapper;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.UserEntity;
import org.sidequest.parley.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private List<User> users;

    @Autowired
    UserRepository userRepository;
    private int USERS_COUNT;

    @Value("${user.avatar.directory}")
    private String uploadDir;
    // private final String uploadDir = "C:\\Users\\jrack\\OneDrive\\Documents\\parley\\images\\users\\";

    public UserService() {
    }

    @PostConstruct
    private void initalizeUsers() {
        this.users = new ArrayList<>();
        userRepository.findAll().forEach(userEntity -> {
            User user = UserMapper.INSTANCE.mapTo(userEntity);
            this.users.add(user);
            //  log.info(user.toString());
        });

        this.USERS_COUNT = this.users.size();
    }

    public List<User> getUsers() {
        return this.users;
    }

    public User getUser(int userId) {
        Optional<UserEntity> userEntity = userRepository.findById((long) userId);
        User user = UserMapper.INSTANCE.mapTo(userEntity);

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
            UserEntity userEntity = UserMapper.INSTANCE.mapTo(user);
            userRepository.save(userEntity);

            System.out.println("User created");
            this.users.add(user);
            this.USERS_COUNT = userCount;
            return user;

        } catch (Exception e) {
            System.out.println("USER NOT CREATED");
            log.error("USER NOT CREATED" + e.getMessage());
        }
        return null;
    }

    public void updateUser(String name, int id) {
        // User user = new User(id, name); // This is the original code. It is commented out because it is not compatible with the new User class.
        User user = new User();
        user.setId(id);
        user.setName(name);

        try {
            UserEntity userEntity = UserMapper.INSTANCE.mapTo(user);
            userRepository.save(userEntity);
            System.out.println("User updated");
        } catch (Exception e) {
            System.out.println("User not updated");
        }
    }

    public void setUserAvatar(int userId, MultipartFile file) throws Exception {
        UserEntity user = userRepository.findById((long) userId).orElseThrow(() -> new Exception("User not found"));

        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            String fileName = user.getId() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, bytes);

            user.setAvatarPath(path.toString());
            userRepository.save(user);
        } else {
            throw new Exception("Empty file!");
        }
    }

    public String getUserAvatar(int userId) throws Exception {
        UserEntity user = userRepository.findById((long) userId).orElseThrow(() -> new Exception("User not found"));
        return user.getAvatarPath();
    }

}
