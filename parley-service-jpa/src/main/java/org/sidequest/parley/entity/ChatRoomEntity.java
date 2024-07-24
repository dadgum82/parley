package org.sidequest.parley.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "ChatRoomEntity")
@Getter
@Setter
@Table(name = "chatrooms")
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EnrollmentEntity> enrollments = new HashSet<>();

    @OneToOne
    private UserEntity moderator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatmessages_id")
    private ChatMessageEntity chatMessage;

    private String name;

    private String iconPath;

    // Constructors, getters, setters, and toString method

    @Override
    public String toString() {
        return "ChatRoomEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", iconPath='" + iconPath + '\'' +
                '}';
    }

}
