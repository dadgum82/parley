package org.sidequest.parley.repository;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "chat_rooms")
public class ChatRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.EAGER)
    private List<ChatRoomUsersEntity> chatRoomUsers;

    private int moderatorId;


    private String name;

    private String iconPath;

    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public List<ChatRoomUsersEntity> getChatRoomUsers() {
        return chatRoomUsers;
    }

    public void setChatRoomUsers(List<ChatRoomUsersEntity> chatRoomUsers) {
        this.chatRoomUsers = chatRoomUsers;
    }

    public int getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(int moderatorId) {
        this.moderatorId = moderatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
}
