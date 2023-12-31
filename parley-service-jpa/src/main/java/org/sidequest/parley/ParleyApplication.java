package org.sidequest.parley;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EntityScan(basePackages = {"org.sidequest.parley.entity"})
public class ParleyApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ParleyApplication.class, args);
    }

    private static final Logger log = LoggerFactory.getLogger(ParleyApplication.class);

}
