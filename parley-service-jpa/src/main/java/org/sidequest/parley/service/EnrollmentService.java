package org.sidequest.parley.service;

import org.sidequest.parley.entity.EnrollmentEntity;
import org.sidequest.parley.mapper.EnrollmentMapper;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.Enrollment;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {
    private static final Logger log = Logger.getLogger(EnrollmentService.class.getName());

    private EnrollmentRepository enrollmentRepository;

    @Autowired
    public void setEnrollmentRepository(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Enrollment> addUsersToChatRoom(List<User> users, ChatRoom chatRoom) {
        List<Enrollment> enrollments = new ArrayList<>();
        log.info("Adding " + users.size() + " users to chat room: " + chatRoom.getName());
        for (User user : users) {
            Enrollment enrollment = addUserToChatRoom(user, chatRoom);
            enrollments.add(enrollment);
        }
        return enrollments;
    }

    @Transactional
    public Enrollment addUserToChatRoom(User user, ChatRoom chatRoom) {
        Long userId = user.getId();
        Long chatRoomId = chatRoom.getChatRoomId();

        if (userId == null) {
            log.severe("User ID cannot be null");
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (chatRoomId == null) {
            log.severe("Chat room ID cannot be null");
            throw new IllegalArgumentException("Chat room ID cannot be null");
        }

        log.info("Adding user: "
                + user.getName()
                + " (" + user.getId() + ") to chat room: "
                + chatRoom.getName()
                + " (" + chatRoom.getChatRoomId() + ")");

        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(userId);
        enrollment.setChatRoomId(chatRoomId);
        EnrollmentEntity enrollmentEntity = EnrollmentMapper.INSTANCE.toEntity(enrollment);
        enrollmentEntity = enrollmentRepository.saveAndFlush(enrollmentEntity); // Save and flush to get the ID
        return EnrollmentMapper.INSTANCE.toModel(enrollmentEntity);
    }

    public Enrollment getChatRoomEnrollment(Long chatRoomId, Long userId) {
        EnrollmentEntity enrollmentEntity = enrollmentRepository.findByChatroomIdAndChatuserId(chatRoomId, userId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        return EnrollmentMapper.INSTANCE.toModel(enrollmentEntity);
    }

    public Set<Long> getChatRoomUserIds(Long chatRoomId) {
        Set<EnrollmentEntity> enrollmentEntities = enrollmentRepository.findByChatroomId(chatRoomId);
        return enrollmentEntities.stream()
                .map(EnrollmentMapper.INSTANCE::toModel)
                .map(Enrollment::getUserId) // Correctly map to user IDs
                .collect(Collectors.toSet());

    }
}
