package org.sidequest.parley.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
public class OpenAPIConfig {
    private static final Logger log = Logger.getLogger(OpenAPIConfig.class.getName());

    @Bean
    public OpenAPI customizeOpenAPI() {
        log.info("Initializing OpenAPI configuration");
        final String securitySchemeName = "bearerAuth";

        OpenAPI openAPI = new OpenAPI()
                .addServersItem(new Server().url("/parley/api").description("Default Server"))
                .info(new Info()
                        .title("Parley API")
                        .version("1.0.0")
                        .description("Chat application API"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Authorization header using Bearer scheme. Example: \"Bearer {token}\"")));

        log.info("OpenAPI configuration completed");
        return openAPI;
    }
}