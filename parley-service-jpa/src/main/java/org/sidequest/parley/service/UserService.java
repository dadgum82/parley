package org.sidequest.parley.service;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.sidequest.parley.entity.ChatRoomEntity;
import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.mapper.ChatRoomMapper;
import org.sidequest.parley.mapper.UserMapper;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.NewUser;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.UserRepository;
import org.sidequest.parley.util.EmailHelper;
import org.sidequest.parley.util.FileSystemHelper;
import org.sidequest.parley.util.TimeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.zone.ZoneRulesException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger log = Logger.getLogger(UserService.class.getName());

    private UserRepository userRepository;

    @Autowired
    @Lazy
    private EnrollmentService enrollmentService;

    @Autowired
    private EmailHelper emailHelper; // We'll need this

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        TimeHelper timeHelper;
        try {
            timeHelper = new TimeHelper();
            String result = timeHelper.getZoneId(timezone).toString();
            return result != null ? result : timeHelper.getDefaultZoneId().toString();
        } catch (ZoneRulesException e) {
            timeHelper = new TimeHelper();
            return timeHelper.getDefaultZoneId().toString();
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

    @Transactional
    public void requestPasswordReset(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        // Generate a unique password reset token
        String resetToken = UUID.randomUUID().toString();

        // Set the token and expiration time in the user entity
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiration(OffsetDateTime.now().plusHours(24)); // Token valid for 24 hours

        // Save the updated user entity
        userRepository.save(user);

        // Send the password reset email
        emailHelper.sendPasswordResetEmail(user.getEmail(), resetToken);
    }

    @Transactional
    public boolean validatePasswordResetToken(String token) {
        UserEntity user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset token"));

        if (user.getPasswordResetTokenExpiration().isBefore(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        return true;
    }

    @Transactional
    public void resetPassword(String token, String newPassword, @NotNull @Size(min = 8) String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        UserEntity user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset token"));

        if (user.getPasswordResetTokenExpiration().isBefore(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        // Encode password before saving
        user.setMagic(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiration(null);
        userRepository.save(user);

        log.info("Password successfully reset for user: " + user.getId());
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getMagic())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Validate new password
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters long");
        }

        // Encode and set new password
        user.setMagic(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        log.info("Password successfully changed for user: " + userId);
    }
}