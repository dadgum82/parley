package org.sidequest.parley.scripts;

import org.sidequest.parley.config.DbConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "org.sidequest.parley")
@Import(DbConfig.class)
public class PasswordViewerApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PasswordViewerApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }
}