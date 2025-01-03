package org.sidequest.parley.scripts.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordViewerScript implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        System.out.println("\nUser Passwords:");
        System.out.println("----------------------------------------");
        System.out.printf("%-20s | %-50s%n", "Username", "Encrypted Password");
        System.out.println("----------------------------------------");

        for (UserEntity user : userRepository.findAll()) {
            System.out.printf("%-20s | %-50s%n", user.getName(), user.getMagic());
        }
        System.out.println("----------------------------------------\n");
    }
}
