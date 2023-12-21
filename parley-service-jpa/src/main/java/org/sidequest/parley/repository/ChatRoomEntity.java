package org.sidequest.parley.repository;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "chat_rooms")
public class ChatRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomUsersEntity> userIds;

    private int moderatorId;

    private String name;

    private String iconPath;

    // getters and setters


    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ChatRoomUsersEntity> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<ChatRoomUsersEntity> userIds) {
        this.userIds = userIds;
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
}
