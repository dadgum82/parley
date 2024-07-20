package org.sidequest.parley.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "EnrollmentEntity")
@Table(name = "chatrooms_chatusers")
public class EnrollmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chatrooms_id")
    private ChatRoomEntity chatroom;

    @ManyToOne
    @JoinColumn(name = "chatusers_id")
    private UserEntity chatuser;

    // Constructors, getters, setters, and toString method

    // Default constructor for JPA
    public EnrollmentEntity() {
    }

    // Convenience constructor
    public EnrollmentEntity(ChatRoomEntity chatroom, UserEntity user) {
        this.chatroom = chatroom;
        this.chatuser = user;
    }

    // toString method to help with debugging and logging
    @Override
    public String toString() {
        return "EnrollmentEntity{" +
                "id=" + id +
                ", chatroom=" + (chatroom != null ? chatroom.getName() : "null") + // Safely call getName if chatroom is not null
                ", user=" + (chatuser != null ? chatuser.getName() : "null") + // Safely call getUsername if user is not null
                '}';
    }

    // The getUser method corrected to return the user properly
    public UserEntity getUser() {
        return chatuser;
    }

    // Setter method for user
    public void setUser(UserEntity user) {
        this.chatuser = user;
    }
}
