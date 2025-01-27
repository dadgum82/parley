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
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Arrays;
import java.util.List;

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

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector).servletPath("/api");
    }
//
//    The key differences between MvcRequestMatcher and antMatchers are:
//
//    Context Awareness:
//
//
//    MvcRequestMatcher is Spring MVC-aware and understands the servlet context and mappings
//    AntMatchers only matches URL patterns without understanding the Spring MVC context
//
//
//    Pattern Handling:
//
//
//    MvcRequestMatcher handles patterns relative to the DispatcherServlet's path
//    AntMatchers treats patterns as absolute paths
//
//
//    Security:
//
//
//    MvcRequestMatcher is more secure as it considers the full request context
//    AntMatchers can be bypassed in some cases due to differences in path handling
//
//
//    Multiple Servlets:
//
//
//    MvcRequestMatcher works correctly with multiple servlets by using servletPath
//    AntMatchers can cause ambiguity with multiple servlets


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("*"));
                    corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    corsConfig.setMaxAge(3600L); // Cache preflight for 1 hour
                    return corsConfig;
                }))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(mvc.pattern("/v3/api-docs/**"),
                                    mvc.pattern("/swagger-ui/**"),
                                    mvc.pattern("/api-docs/**"),
                                    mvc.pattern("/parley/api-docs/**"),
                                    mvc.pattern("/parley/api/api-docs/**")).permitAll()
                            .requestMatchers(mvc.pattern("/auth/login"),
                                    mvc.pattern("/auth/signup"),
                                    mvc.pattern("/auth/signup/**"),
                                    mvc.pattern("/auth/password/reset"),
                                    mvc.pattern("/auth/password/reset/**")).permitAll()
                            .anyRequest().authenticated();
                })
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
//
//    private static final String[] TOMCAT_WHITE_LIST_URL = {
//            "/webjars/**",
//            "META-INF/resources/webjars/",
//            "META-INF/resources/webjars/**",
//            "/resources/**",
//            "/static/**",
//            "/public/**"
//    };
//
//    private static final String[] SWAGGER_WHITE_LIST_URL = {
//            "/api/swagger-ui/index.html",
//            "/parley/api/api-docs/swagger-config",
//            "/parley/api/swagger-ui/**",
//            "/parley/api/v3/api-docs/**",
//            "/parley/api/swagger-resources/**",
//            "/parley/api/webjars/**",
//            "/swagger-ui/**",
//            "/v3/api-docs/**"
//    };
//
//    private static final String[] PARLEY_WHITE_LIST_URL = {
//            "/parley/api/auth/login",
//            "/parley/api/auth/signup",
//            "/parley/api/auth/signup/**",
//            "/parley/api/auth/password/reset",
//            "/parley/api/auth/password/reset/**",
//            "/auth/signup",
//            "/auth/login",
//            "/auth/logout",
//            "/auth/password/reset",
//            "/api/auth/signup",
//            "/api/auth/login",
//            "/api/auth/logout",
//            "/api/auth/password/reset",
//            "/parley/auth/login",
//            "/parley/auth/signup",
//            "/parley/signup"
//    };
//
//

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        // Multiple logging approaches
//        log.debug("Tomcat Whitelist URLs:");
//        for (String url : TOMCAT_WHITE_LIST_URL) {
//            log.debug("  - {}", url);
//        }
//
//        log.debug("Swagger Whitelist URLs:");
//        for (String url : SWAGGER_WHITE_LIST_URL) {
//            log.debug("  - {}", url);
//        }
//
//        log.debug("Parley Whitelist URLs:");
//        for (String url : PARLEY_WHITE_LIST_URL) {
//            log.debug("  - {}", url);
//        }
//
//        // Log the total number of whitelisted URLs
//        log.debug("Total Whitelisted URLs:");
//        log.debug("  Tomcat URLs: {}", TOMCAT_WHITE_LIST_URL.length);
//        log.debug("  Swagger URLs: {}", SWAGGER_WHITE_LIST_URL.length);
//        log.debug("  Parley URLs: {}", PARLEY_WHITE_LIST_URL.length);
//
//
//        System.out.println("SYSTEM OUT: Configuring SecurityFilterChain");
//        System.err.println("SYSTEM ERR: Configuring SecurityFilterChain");
//
//        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        log.debug("~~~Configuring SecurityFilterChain~~~");
//        log.debug("HTTP Security Configuration Details:");
//        log.debug("HTTP Object: {}", http);
//        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        System.out.println("~~~Configuring SecurityFilterChain~~~");
//        System.out.println("HTTP Security Configuration Details:");
//        System.out.println("HTTP Object: " + http);
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//
//        http
//                .csrf(csrf -> {
//                    csrf.disable();
//                    log.debug("CSRF disabled");
//                })
//                .authorizeHttpRequests(auth -> {
//                    try {
//                        auth
//                                .requestMatchers(
//                                        "/v3/api-docs/**",
//                                        "/swagger-ui/**",
//                                        "/api-docs/**",
//                                        "/parley/api-docs/**",
//                                        "/parley/api/api-docs/**",
//                                        "/parley/api/swagger-ui/**",
//                                        "/parley/api/v3/api-docs/**",
//                                        "/parley/api/swagger-resources/**",
//                                        "/webjars/**"
//                                ).permitAll()
//                                // Public endpoints
//                                .requestMatchers("/parley/api/auth/**").permitAll()
//                                .requestMatchers(TOMCAT_WHITE_LIST_URL).permitAll()
//                                .requestMatchers(SWAGGER_WHITE_LIST_URL).permitAll()
//                                .requestMatchers(PARLEY_WHITE_LIST_URL).permitAll()
//                                .anyRequest()
//                                .authenticated();
//                        // Detailed logging of specific matcher configurations
//                        log.debug("Permitted Tomcat URLs: {}", Arrays.toString(TOMCAT_WHITE_LIST_URL));
//                        log.debug("Permitted Swagger URLs: {}", Arrays.toString(SWAGGER_WHITE_LIST_URL));
//                        log.debug("Permitted Parley URLs: {}", Arrays.toString(PARLEY_WHITE_LIST_URL));
//                    } catch (Exception e) {
//                        log.error("Error configuring request authorization", e);
//                        throw new RuntimeException(e);
//                    }
//                })
//                .sessionManagement(session -> {
//                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                    log.debug("Session management set to STATELESS");
//                })
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        log.debug("SecurityFilterChain configuration complete");
//        return http.build();
//    }

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