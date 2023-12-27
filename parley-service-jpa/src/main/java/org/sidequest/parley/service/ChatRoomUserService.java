package org.sidequest.parley.service;

import org.sidequest.parley.repository.ChatRoomsUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomUserService {
    @Autowired
    private ChatRoomsUsersRepository repository;

    public ChatRoomUserService() {

    }

    public boolean isUserInChatRoom(Long userId, Long chatRoomId) {
        return repository.findByUserIdAndChatRoomId(userId, chatRoomId).isPresent();
    }
}