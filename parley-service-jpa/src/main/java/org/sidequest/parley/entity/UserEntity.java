package org.sidequest.parley.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "UserEntity")
@Getter
@Setter
@Table(name = "chatusers")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "VARCHAR(255)")
    private String name;

    private String avatarPath;

    @Column(name = "timezone", columnDefinition = "VARCHAR(255)")
    private String timezone;

    @CreatedDate
    @Column(name = "created", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated")
    private OffsetDateTime updatedAt;

    @Setter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "last_posted_message_date_time")
    private OffsetDateTime lastPostedMessageDateTime;

    // Define the relationship with EnrollmentEntity
    @OneToMany(mappedBy = "chatuser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EnrollmentEntity> enrollments = new HashSet<>();

    // Constructors, getters, setters, and toString method

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                ", timezone='" + timezone + '\'' +
                ", lastPostedMessageDateTime=" + lastPostedMessageDateTime +
                '}';
    }

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }

}
