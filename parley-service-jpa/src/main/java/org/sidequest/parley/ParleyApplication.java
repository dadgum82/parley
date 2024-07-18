package org.sidequest.parley;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.logging.Logger;

@SpringBootApplication
@ComponentScan(basePackages = {"org.sidequest.parley.controller", "org.sidequest.parley.service"})
//@Import(org.sidequest.parley.config.ServiceConfig.class)
public class ParleyApplication extends SpringBootServletInitializer {
    private static final Logger log = Logger.getLogger(ParleyApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(ParleyApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            // This code will run on startup
            log.info("Application has started.");
        };
    }
}
