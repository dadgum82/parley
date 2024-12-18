package org.sidequest.parley.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sidequest.parley.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    // Static initialization to test logging
    static {
        System.out.println("STATIC INIT: SecurityConfig is being loaded");
    }

    private static final String[] TOMCAT_WHITE_LIST_URL = {
            "/webjars/**",
            "META-INF/resources/webjars/",
            "META-INF/resources/webjars/**",
            "/resources/**",
            "/static/**",
            "/public/**"
    };

    private static final String[] SWAGGER_WHITE_LIST_URL = {
            "/parley/api/api-docs/swagger-config",
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/api-docs",
            "/api-docs/swagger-config",
            "/parley/api-docs/**",
            "/parley/api-docs",
            "/parley/api-docs/swagger-config",
            "/parley/api/api-docs/**",
            "/parley/api/api-docs",
            "/parley/api/api-docs/swagger-config",
            "/configuration/ui",
            "/configuration/security",
            "/parley",
            "/parley/**",
            "/parley/api",
            "/parley/api/",
            "/parley/api/api-docs/**",
            "/parley/api/auth/**",
            "/parley/api/api-docs/swagger-config",
            "/parley/api/api-docs",
            "/parley/api/api-docs/**",
            "/parley/parley/api/error",
            "/parley/api/swagger-ui.html",
            "/parley/api/swagger-ui/**",
            "/parley/api/test/**",
            "/parley/api/v1/auth/**",
            "/parley/api/api-docs/swagger-config",  // for swagger config
            "/parley/api/swagger-ui/index.html",    // for the main UI page
            "/parley/api/swagger-ui.html",          // alternative entry point
            "/parley/api/swagger-initializer.js",
            "/parley/api/v3/api-docs/swagger-config", // for OpenAPI 3 config
            "/parley/api/swagger-ui/**",            // for UI resources
            "/parley/api/v3/api-docs",             // for OpenAPI docs
            "/parley/api/webjars/**",               // for web resources
            "/parley/swagger-ui/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/api/api-docs/swagger-config",
            "/api/swagger-ui/swagger-initializer.js",
            "/api/swagger-ui/",
            "/api/swagger-ui/**",
            "/api/api-docs/swagger-config",
            "api/api-docs/**",
            "/api/error",
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v2/api-docs/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources",
            "/swagger-resources/**"
    };

    private static final String[] PARLEY_WHITE_LIST_URL = {
            "/parley/api/auth/login",
            "/parley/api/auth/signup",
            "/parley/api/auth/password/reset",
            "/parley/api/auth/password/reset/**",
            "/auth/signup",
            "/auth/login",
            "/auth/logout",
            "/auth/password/reset",
            "/api/auth/signup",
            "/api/auth/login",
            "/api/auth/logout",
            "/api/auth/password/reset"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Multiple logging approaches
        log.debug("Tomcat Whitelist URLs:");
        for (String url : TOMCAT_WHITE_LIST_URL) {
            log.debug("  - {}", url);
        }

        log.debug("Swagger Whitelist URLs:");
        for (String url : SWAGGER_WHITE_LIST_URL) {
            log.debug("  - {}", url);
        }

        log.debug("Parley Whitelist URLs:");
        for (String url : PARLEY_WHITE_LIST_URL) {
            log.debug("  - {}", url);
        }

        // Log the total number of whitelisted URLs
        log.debug("Total Whitelisted URLs:");
        log.debug("  Tomcat URLs: {}", TOMCAT_WHITE_LIST_URL.length);
        log.debug("  Swagger URLs: {}", SWAGGER_WHITE_LIST_URL.length);
        log.debug("  Parley URLs: {}", PARLEY_WHITE_LIST_URL.length);


        System.out.println("SYSTEM OUT: Configuring SecurityFilterChain");
        System.err.println("SYSTEM ERR: Configuring SecurityFilterChain");

        log.error("ERROR LEVEL: Configuring SecurityFilterChain");
        log.warn("WARN LEVEL: Configuring SecurityFilterChain");
        log.info("INFO LEVEL: Configuring SecurityFilterChain");
        log.debug("DEBUG LEVEL: Configuring SecurityFilterChain");
        log.trace("TRACE LEVEL: Configuring SecurityFilterChain");

        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        log.debug("~~~Configuring SecurityFilterChain~~~");
        log.debug("HTTP Security Configuration Details:");
        log.debug("HTTP Object: {}", http);
        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("~~~Configuring SecurityFilterChain~~~");
        System.out.println("HTTP Security Configuration Details:");
        System.out.println("HTTP Object: " + http);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        http
                .csrf(csrf -> {
                    csrf.disable();
                    log.debug("CSRF disabled");
                })
                .authorizeHttpRequests(auth -> {
                    try {
                        auth
                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/api-docs/**",
                                        "/parley/api-docs/**",
                                        "/parley/api/api-docs/**"
                                ).permitAll()
                                .requestMatchers(TOMCAT_WHITE_LIST_URL).permitAll()
                                .requestMatchers(SWAGGER_WHITE_LIST_URL).permitAll()
                                .requestMatchers(PARLEY_WHITE_LIST_URL).permitAll()
                                .anyRequest()
                                .authenticated();
                        // Detailed logging of specific matcher configurations
                        log.debug("Permitted Tomcat URLs: {}", Arrays.toString(TOMCAT_WHITE_LIST_URL));
                        log.debug("Permitted Swagger URLs: {}", Arrays.toString(SWAGGER_WHITE_LIST_URL));
                        log.debug("Permitted Parley URLs: {}", Arrays.toString(PARLEY_WHITE_LIST_URL));
                    } catch (Exception e) {
                        log.error("Error configuring request authorization", e);
                        throw new RuntimeException(e);
                    }
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    log.debug("Session management set to STATELESS");
                })
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        log.debug("SecurityFilterChain configuration complete");
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        log.debug("Creating AuthenticationProvider");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        log.debug("AuthenticationProvider created with UserDetailsService and PasswordEncoder");
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.debug("Creating AuthenticationManager");
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.debug("Creating BCryptPasswordEncoder");
        return new BCryptPasswordEncoder();
    }
}