package org.sidequest.parley.controller;

import org.sidequest.parley.api.EnrollmentsApi;
import org.sidequest.parley.model.Enrollment;
import org.sidequest.parley.model.NewEnrollment;
import org.sidequest.parley.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class EnrollmentController implements EnrollmentsApi {

    private static final Logger log = Logger.getLogger(EnrollmentController.class.getName());

    @Autowired
    private EnrollmentService enrollmentService;

    @Override
    public ResponseEntity<List<Enrollment>> addUsersToChatRooms(List<NewEnrollment> enrollment) {
        try {
            List<Enrollment> enrollments = enrollmentService.addUsersToChatRoom(enrollment);
            return ResponseEntity.status(HttpStatus.CREATED).body(enrollments);
        } catch (Exception e) {
            log.severe("Error adding users to chat rooms: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> addUserToChatRoom(Long chatRoomId, Long userId) {
        try {
            Enrollment newEnrollment = enrollmentService.addUserToChatRoom(chatRoomId, userId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            log.severe("Error adding user to chat room: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<Enrollment>> getEnrollmentsByChatRoomId(Long chatRoomId) {
        try {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByChatRoomId(chatRoomId);
            return ResponseEntity.status(HttpStatus.CREATED).body(enrollments);
        } catch (Exception e) {
            log.severe("Error getting enrollments by chat room id: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    @Override
    public ResponseEntity<Void> removeUserFromChatRoom(Long chatRoomId, Long userId) {
        try {
            enrollmentService.removeUserFromChatRoom(chatRoomId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.severe("Error removing user from chat room: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> removeUsersFromChatRoom(Long chatRoomId) {
        try {
            enrollmentService.removeUsersFromChatRoom(chatRoomId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.severe("Error removing users from chat room: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
