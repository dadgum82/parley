package org.sidequest.parley.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "chat_rooms")
public class ChatRoomEntity {
    public ChatRoomEntity() {
    }

    public ChatRoomEntity(Long id, List<ChatRoomsUsersEntity> chatRoomUsers, UserEntity moderator, String name, String iconPath) {
        this.id = id;
        this.chatRoomUsers = chatRoomUsers;
        this.moderator = moderator;
        this.name = name;
        this.iconPath = iconPath;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.EAGER)
    private List<ChatRoomsUsersEntity> chatRoomUsers;

    @OneToOne
    private UserEntity moderator;

    private String name;

    private String iconPath;

    public List<UserEntity> getUsers() {
        List<UserEntity> users = new ArrayList<>();
        for (ChatRoomsUsersEntity chatRoomUser : chatRoomUsers) {
            users.add(chatRoomUser.getUser());
        }
        return users;
    }

    public void deleteChatRoomUsers() {
        chatRoomUsers.clear();
    }

    public void deleteChatRoomUser(UserEntity user) {
        chatRoomUsers.removeIf(chatRoomsUsersEntity -> chatRoomsUsersEntity.getUser().getId().equals(user.getId()));
    }
}
