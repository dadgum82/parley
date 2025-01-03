package org.sidequest.parley.scripts.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordResetScript implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        String defaultPassword = "password";
        String encodedPassword = passwordEncoder.encode(defaultPassword);

        try {
            int updatedCount = 0;
            for (UserEntity user : userRepository.findAll()) {
                user.setMagic(encodedPassword);
                userRepository.save(user);
                updatedCount++;
                log.info("Updated password for user: {}", user.getName());
            }
            log.info("Successfully updated passwords for {} users", updatedCount);

        } catch (Exception e) {
            log.error("Error updating passwords: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update passwords", e);
        }
    }
}