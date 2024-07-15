package org.sidequest.parley.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

@Entity(name = "ChatMessageEntity")
@Getter
@Setter
@Table(name = "chatmessages")
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private ChatRoomEntity chatRoom;
    private String content;
    private String screenEffect;
    private String textEffect;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "chatusers_id")
    private UserEntity user;

}
