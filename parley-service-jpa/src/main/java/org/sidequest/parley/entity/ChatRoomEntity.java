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
}
