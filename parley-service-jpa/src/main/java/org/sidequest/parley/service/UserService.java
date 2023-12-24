package org.sidequest.parley.service;

import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.mapper.UserMapper;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Value("${user.avatar.directory}")
    private String uploadDir;

    public UserService() {
    }

    public List<User> getUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper.INSTANCE::toModel)
                .collect(Collectors.toList());
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .map(UserMapper.INSTANCE::toModel)
                .orElse(null);
    }

    public User createUser(String name) {
        User user = new User();
        user.setName(name);
        UserEntity userEntity = UserMapper.INSTANCE.toEntity(user);
        userEntity = userRepository.save(userEntity);
        return UserMapper.INSTANCE.toModel(userEntity);
    }

    public void updateUser(String name, Long id) {
        UserEntity userEntity = userRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userEntity.setName(name);
        userRepository.save(userEntity);
    }

    public void setUserAvatar(Long userId, MultipartFile file) throws Exception {
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

    public String getUserAvatar(Long userId) throws Exception {
        UserEntity user = userRepository.findById((long) userId).orElseThrow(() -> new Exception("User not found"));
        return user.getAvatarPath();
    }
}