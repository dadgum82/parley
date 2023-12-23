package org.sidequest.parley.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "chat_rooms")
public class ChatRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.EAGER)
    private List<ChatRoomUsersEntity> chatRoomUsers;

    @OneToOne
    private UserEntity moderator;

    private String name;

    private String iconPath;

    // getters and setters
}
