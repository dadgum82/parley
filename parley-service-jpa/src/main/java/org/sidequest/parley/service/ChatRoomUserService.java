package org.sidequest.parley.service;

import org.sidequest.parley.repository.ChatRoomUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomUserService {
    @Autowired
    private ChatRoomUsersRepository repository;

    public ChatRoomUserService() {

    }

    public boolean isUserInChatRoom(int userId, int chatRoomId) {
        return repository.findByUserIdAndChatRoomId(userId, chatRoomId).isPresent();
    }
}