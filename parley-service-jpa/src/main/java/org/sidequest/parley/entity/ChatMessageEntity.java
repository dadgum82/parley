package org.sidequest.parley.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

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
@Table(name = "chat_messages")
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    private int chatRoomId;
    private String content;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
