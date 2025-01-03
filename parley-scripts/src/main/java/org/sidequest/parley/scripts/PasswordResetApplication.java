package org.sidequest.parley.scripts;

import org.sidequest.parley.config.DbConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "org.sidequest.parley")
@Import(DbConfig.class)
public class PasswordResetApplication {
    public static void main(String[] args) {
        SpringApplication.run(PasswordResetApplication.class, args);
    }
}