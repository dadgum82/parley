package org.sidequest.parley.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "chat_room_users")
public class ChatRoomUsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", referencedColumnName = "id")
    private ChatRoomEntity chatRoom;

    @OneToOne
    private UserEntity moderator;

}