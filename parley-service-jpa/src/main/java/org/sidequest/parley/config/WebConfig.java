package org.sidequest.parley.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Swagger UI resources
        registry
                .addResourceHandler("/swagger-ui/**", "/api/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/4.15.5/")
                .resourceChain(false);

        // WebJars resources
        registry
                .addResourceHandler("/webjars/**", "/api/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .resourceChain(false);

        // Other static resources
        registry
                .addResourceHandler("/**")
                .addResourceLocations(
                        "classpath:/META-INF/resources/",
                        "classpath:/resources/",
                        "classpath:/static/",
                        "classpath:/public/"
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        // TODO: Update allowedOrigins with your production domain
//        registry.addMapping("/**")
//                .allowedOrigins(
//                        "http://localhost:3000",     // React development server
//                        "http://localhost:8080",     // Your local backend
//                        "http://your-production-domain.com"  // Your production domain
//                )
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
//                .allowedHeaders("Authorization", "Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers")
//                .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
//                .allowCredentials(true)
//                .maxAge(3600); // 1 hour max age

        registry.addMapping("/**")
                .allowedOriginPatterns("*")  // More permissive than allowedOrigins("*")
                .allowedMethods("*")         // Allows all methods
                .allowedHeaders("*")         // Allows all headers
                .allowCredentials(true)      // Allows credentials
                .maxAge(3600);              // 1 hour max age
    }
}