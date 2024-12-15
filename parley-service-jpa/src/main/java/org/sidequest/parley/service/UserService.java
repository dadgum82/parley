package org.sidequest.parley.service;

import org.sidequest.parley.entity.ChatRoomEntity;
import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.helpers.FileSystemHelper;
import org.sidequest.parley.mapper.ChatRoomMapper;
import org.sidequest.parley.mapper.UserMapper;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.NewUser;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.zone.ZoneRulesException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger log = Logger.getLogger(UserService.class.getName());

    private UserRepository userRepository;

    @Autowired
    @Lazy
    private EnrollmentService enrollmentService;

    @Value("${user.avatar.directory}")
    private String userAvatarDirectory;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private FileSystemHelper fileSystemHelper;

    public List<User> getUsers() {
        return userRepository.findAll().stream().map(UserMapper.INSTANCE::toModel).collect(Collectors.toList());
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).map(UserMapper.INSTANCE::toModel).orElse(null);
    }

    @Transactional
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

    @Transactional
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


    public List<ChatRoom> getChatRoomsByUserId(Long userId) {
        try {
            if (userId == null) {
                log.warning("User ID cannot be null");
                throw new IllegalArgumentException("User ID cannot be null");
            }

            if (!userRepository.existsById(userId)) {
                log.warning("User not found with id: " + userId);
                throw new IllegalArgumentException("User not found with id: " + userId);
            }

            List<ChatRoom> chatRooms = enrollmentService.getChatRoomsByUserId(userId);
            log.info("Found " + chatRooms.size() + " chat rooms for user " + userId);
            return chatRooms;
        } catch (IllegalArgumentException e) {
            log.severe("Invalid request for user chatrooms: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.severe("Error getting chat rooms for user " + userId + ": " + e.getMessage());
            throw new RuntimeException("Error retrieving chat rooms", e);
        }
    }

    private ChatRoom mapChatRoomEntityToChatRoom(ChatRoomEntity entity) {
        return ChatRoomMapper.INSTANCE.toModel(entity);
    }

    @Transactional
    public void updateLastPostedMessageDateTime(Long userId, OffsetDateTime odt) {
        log.info("Updating last posted message date time for user: " + userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userEntity.setLastPostedMessageDateTime(odt);
        userRepository.save(userEntity);
    }

    @Transactional
    public void setUserAvatar(Long userId, MultipartFile avatarFile) throws Exception {
        if (!avatarFile.isEmpty()) {
            log.fine("Saving avatar for user: " + userId);
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
            String path = fileSystemHelper.saveFile(avatarFile, userAvatarDirectory);
            user.setAvatarPath(path);
            userRepository.save(user);
        } else {
            log.severe("Empty file!");
            throw new Exception("Empty file!");
        }
    }

    public String getUserAvatar(Long userId) throws Exception {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        return user.getAvatarPath();
    }

    public String getUserTimezone(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String strUserTimezone = userEntity.getTimezone();
        return getZoneId(strUserTimezone);
    }

    public UserEntity getUserEntity(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserByName(String name) {
        UserEntity userEntity = userRepository.findByName(name).orElse(null);
        if (userEntity == null) {
            return null;
        }
        return UserMapper.INSTANCE.toModel(userEntity);
    }

    public String getUserMagic(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userEntity.getMagic();
    }

    @Transactional
    public User createUserWithPassword(String name, String password, String timezone) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // TODO  In the future, we should hash the password before storing it
        User user = new User();
        user.setName(name);
        user.setTimezone(getZoneId(timezone));

        UserEntity userEntity = UserMapper.INSTANCE.toEntity(user);
        userEntity.setMagic(password); // Store password in magic field

        userEntity = userRepository.save(userEntity);
        return UserMapper.INSTANCE.toModel(userEntity);
    }
}