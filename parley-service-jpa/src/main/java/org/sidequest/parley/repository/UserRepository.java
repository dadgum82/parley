package org.sidequest.parley.repository;

import org.sidequest.parley.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository // This is an addition to try the mapper
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByName(String name);

    boolean existsByName(String name);

    void deleteByName(String name);

    // New methods for password reset functionality
    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Query("UPDATE UserEntity u SET u.passwordResetToken = :token, u.passwordResetTokenExpiration = :expiration WHERE u.email = :email")
    void updatePasswordResetToken(@Param("email") String email, @Param("token") String token, @Param("expiration") OffsetDateTime expiration);

    Optional<UserEntity> findByPasswordResetToken(String token);

    @Modifying
    @Query("UPDATE UserEntity u SET u.magic = :password, u.passwordResetToken = NULL, u.passwordResetTokenExpiration = NULL WHERE u.id = :userId")
    void updatePassword(@Param("userId") Long userId, @Param("password") String password);

    boolean existsByEmail(String email);
}
