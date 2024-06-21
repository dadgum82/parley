package org.sidequest.parley.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.List;

//import lombok.Getter;
//import lombok.Setter;
//import lombok.experimental.Accessors;
//
//@Getter
//@Setter
//@Accessors(chain = true)
@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity {
    public UserEntity() {
    }

    public UserEntity(Long id, String name, String avatarPath, String timezone, OffsetDateTime lastPostedMessageDateTime, List<ChatRoomsUsersEntity> chatRoomUsers) {
        this.id = id;
        this.name = name;
        this.avatarPath = avatarPath;
        this.timezone = timezone;
        this.lastPostedMessageDateTime = lastPostedMessageDateTime;
        this.chatRoomUsers = chatRoomUsers;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "VARCHAR(255)")
    private String name;

    private String avatarPath;

    @Column(name = "timezone", columnDefinition = "VARCHAR(255)")
    private String timezone;

    @Setter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "lastPostedMessageDateTime")
    private OffsetDateTime lastPostedMessageDateTime;

    @OneToMany(mappedBy = "user")
    private List<ChatRoomsUsersEntity> chatRoomUsers;

}
