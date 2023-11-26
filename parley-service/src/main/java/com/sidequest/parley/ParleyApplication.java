package com.sidequest.parley;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ParleyApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ParleyApplication.class, args);
    }

    //    https://www.java4s.com/spring-boot-tutorials/spring-boot-configure-datasource-using-jndi-with-example/
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ParleyApplication.class);
    }

}
