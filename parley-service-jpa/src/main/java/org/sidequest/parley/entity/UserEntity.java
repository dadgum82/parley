package org.sidequest.parley.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "UserEntity")
@Getter
@Setter
@Table(name = "chatusers")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "magic", columnDefinition = "VARCHAR(255)")
    private String magic;

    private String avatarPath;

    @Column(name = "timezone", columnDefinition = "VARCHAR(255)")
    private String timezone;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "passwordresettoken")
    private String passwordResetToken;

    @Column(name = "passwordresettokenexpiration")
    private OffsetDateTime passwordResetTokenExpiration;


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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return magic; // using the 'magic' field as password
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
