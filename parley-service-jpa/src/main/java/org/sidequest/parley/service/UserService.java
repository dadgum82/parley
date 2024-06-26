package org.sidequest.parley.service;

import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.mapper.UserMapper;
import org.sidequest.parley.model.NewUser;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.zone.ZoneRulesException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger log = Logger.getLogger(UserService.class.getName());
    @Autowired
    UserRepository userRepository;

    @Value("${user.avatar.directory}")
    private String uploadDir;

    public UserService() {
    }

    public List<User> getUsers() {
        return userRepository.findAll().stream().map(UserMapper.INSTANCE::toModel).collect(Collectors.toList());
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).map(UserMapper.INSTANCE::toModel).orElse(null);
    }

    public User createUser(String name, String timeZone) {
        log.info("Creating user: " + name + " with timezone: " + timeZone);
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        String timezone = getZoneId(timeZone);

        User user = new User();
        user.setName(name);
        user.setTimezone(timezone);
        UserEntity userEntity = UserMapper.INSTANCE.toEntity(user);
        userEntity = userRepository.save(userEntity);
        log.info("User created: " + userEntity.toString());
        return UserMapper.INSTANCE.toModel(userEntity);
    }

    private String getZoneId(String timezone) {
        try {
            if (timezone == null || timezone.trim().isEmpty()) {
                ZoneId estZoneId = ZoneId.of("America/New_York");
                timezone = estZoneId.toString();
            } else if (!isTimezone(timezone)) {
                ZoneId estZoneId = ZoneId.of("America/New_York");
                timezone = estZoneId.toString();
            }
            return timezone;
        } catch (ZoneRulesException e) {
            return ZoneId.of("America/New_York").toString();
        }
    }

    private boolean isTimezone(String timezone) {
        try {
            ZoneId.of(timezone);
            return true;
        } catch (ZoneRulesException e) {
            return false;
        }
    }

    public User updateUserById(Long id, NewUser user) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getName() != null && !user.getName().isEmpty()) {
            userEntity.setName(user.getName());
        }
        if (user.getTimezone() != null && !user.getTimezone().isEmpty()) {
            String timezone = getZoneId(user.getTimezone());
            userEntity.setTimezone(timezone);
        }


        userEntity = userRepository.save(userEntity);
        return UserMapper.INSTANCE.toModel(userEntity);
    }

    public void setUserAvatar(Long userId, MultipartFile file) throws Exception {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));

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
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        return user.getAvatarPath();
    }
}