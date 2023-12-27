package org.sidequest.parley.repository;

import org.sidequest.parley.entity.ChatRoomsUsersEntity;
import org.sidequest.parley.model.ChatRoomUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomsUsersRepository extends JpaRepository<ChatRoomsUsersEntity, Long> {
    Optional<ChatRoomUsers> findByUserIdAndChatRoomId(Long userId, Long chatRoomId);
}