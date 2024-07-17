package org.sidequest.parley.controller;

import org.sidequest.parley.api.UsersApi;
import org.sidequest.parley.model.NewUser;
import org.sidequest.parley.model.User;
import org.sidequest.parley.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.logging.Logger;

/**
 * A RESTful web service controller for managing users.
 */
//@Service
@RestController
public class UserController implements UsersApi {
    private static final Logger log = Logger.getLogger(UserController.class.getName());


    UserService userService;

    @Autowired
    public UserController(@Lazy UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a list of all users in JSON format.
     *
     * @return A JSON-formatted string containing an array of users.
     */
    @Override
//    public ResponseEntity<List<org.sidequest.parley.model.User>> getUsers() {
    public ResponseEntity<List<User>> getUsers() {
        try {
            List<User> users = userService.getUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<User> getUserById(Long id) {
        try {
            User user = userService.getUser(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<User> createUser(NewUser newUser) {
        try {
            log.fine("createUser: " + newUser.getName() + " " + newUser.getTimezone());
            User user = userService.createUser(newUser.getName(), newUser.getTimezone());
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.severe("Error creating user:" + e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.severe("Error creating user:" + e);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> setUserAvatar(Long id, MultipartFile file) {
        try {
            userService.setUserAvatar(id, file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Resource> getUserAvatar(Long id) {
        try {
            String avatarPath = userService.getUserAvatar(id);
            File file = new File(avatarPath);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + file.getName());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.IMAGE_PNG) // or MediaType.IMAGE_PNG if it's a PNG image MediaType.IMAGE_JPEG
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<User> updateUserById(Long id, NewUser newUser) {
        try {
            User user = userService.updateUserById(id, newUser);
            //return ResponseEntity.ok().build();
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}