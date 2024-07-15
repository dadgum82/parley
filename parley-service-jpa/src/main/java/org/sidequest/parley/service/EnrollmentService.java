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

import java.util.List;
import java.util.logging.Logger;

@Service
public class EnrollmentService {
    private static final Logger log = Logger.getLogger(EnrollmentService.class.getName());

    @Autowired
    EnrollmentRepository enrollmentRepository;

    public List<Enrollment> addUsersToChatRoom(List<User> users, ChatRoom chatRoom) {
        return users.stream().map(user -> addUserToChatRoom(user, chatRoom)).toList();
    }

    @Transactional
    public Enrollment addUserToChatRoom(User user, ChatRoom chatRoom) {
        log.info("Adding user: " + user.getName() + " to chat room: " + chatRoom.getName());
        Long userId = user.getId();
        Long chatRoomId = chatRoom.getChatRoomId();

        if (userId == null || chatRoomId == null) {
            log.severe("User ID and Chat Room ID cannot be null");
            throw new IllegalArgumentException("User ID and Chat Room ID cannot be null");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(userId);
        enrollment.setChatRoomId(chatRoomId);
        EnrollmentEntity enrollmentEntity = EnrollmentMapper.INSTANCE.toEntity(enrollment);
        enrollmentEntity = enrollmentRepository.save(enrollmentEntity);
        return EnrollmentMapper.INSTANCE.toModel(enrollmentEntity);

    }
}
