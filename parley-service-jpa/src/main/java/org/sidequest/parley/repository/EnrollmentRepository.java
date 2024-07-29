package org.sidequest.parley.repository;

import org.sidequest.parley.entity.EnrollmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface EnrollmentRepository extends JpaRepository<EnrollmentEntity, Long> {
    Optional<EnrollmentEntity> findByChatroomIdAndChatuserId(Long chatroomId, Long userId);

    Set<EnrollmentEntity> findByChatroomId(Long chatroomId);

    void deleteByChatroomId(Long chatRoomId);

    void deleteByChatroomIdAndChatuserId(Long chatRoomId, Long userId);

    boolean existsByChatroomIdAndChatuserId(Long chatRoomId, Long userId);
}
