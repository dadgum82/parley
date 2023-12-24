package org.sidequest.parley.repository;

import org.sidequest.parley.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // This is an addition to try the mapper
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

}