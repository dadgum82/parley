package org.sidequest.parley.repository;

import org.sidequest.parley.entity.ChatRoomUsersEntity;
import org.sidequest.parley.model.ChatRoomUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ChatRoomUsersRepository extends JpaRepository<ChatRoomUsersEntity, Long> {
    Optional<ChatRoomUsers> findByUserIdAndChatRoomId(Long userId, Long chatRoomId);
}