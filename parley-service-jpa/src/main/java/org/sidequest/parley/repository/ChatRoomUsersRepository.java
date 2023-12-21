package org.sidequest.parley.repository;

import org.sidequest.parley.model.ChatRoomUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ChatRoomUsersRepository extends JpaRepository<ChatRoomUsersEntity, Long> {
    Optional<ChatRoomUsers> findByUserIdAndChatRoomId(int userId, int chatRoomId);
}