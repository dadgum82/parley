package org.sidequest.parley.repository;

import org.sidequest.parley.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // This is an addition to try the mapper
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
//    @Query("SELECT c FROM ChatRoomEntity c JOIN FETCH c.chatRoomUsers u JOIN FETCH u.user")
//    List<ChatRoomEntity> findAllWithUsers();

}