//package org.sidequest.parley.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
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
//            "/configuration/ui",
//            "/configuration/security",
//            "/parley",
//            "/parley/**",
//            "/parley/api",
//            "/parley/api/",
//            "/parley/api/api-docs/**",
//            "/parley/api/auth/**",
//            "/parley/api/api-docs/swagger-config",
//            "/parley/api/api-docs",
//            "/parley/api/api-docs/**",
//            "/parley/parley/api/error",
//            "/parley/api/swagger-ui.html",
//            "/parley/api/swagger-ui/**",
//            "/parley/api/test/**",
//            "/parley/api/v1/auth/**",
//            "/parley/api/api-docs/swagger-config",  // for swagger config
//            "/parley/api/swagger-ui/index.html",    // for the main UI page
//            "/parley/api/swagger-ui.html",          // alternative entry point
//            "/parley/api/swagger-initializer.js",
//            "/parley/api/v3/api-docs/swagger-config", // for OpenAPI 3 config
//            "/parley/api/swagger-ui/**",            // for UI resources
//            "/parley/api/v3/api-docs",             // for OpenAPI docs
//            "/parley/api/webjars/**",               // for web resources
//            "/parley/swagger-ui/**",
//            "/v2/api-docs",
//            "/v3/api-docs",
//            "/v3/api-docs/**",
//            "/api/api-docs/swagger-config",
//            "/api/swagger-ui/swagger-initializer.js",
//            "/api/swagger-ui/",
//            "/api/swagger-ui/**",
//            "/api/api-docs/swagger-config",
//            "api/api-docs/**",
//            "/api/error",
//            "/api/v1/auth/**",
//            "/v2/api-docs",
//            "/v2/api-docs/**",
//            "/v3/api-docs",
//            "/v3/api-docs/**",
//            "/swagger-resources",
//            "/swagger-resources/**",
//            "/swagger-ui.html",
//            "/swagger-ui/**",
//            "/swagger-resources",
//            "/swagger-resources/**"
//    };
//
//    private static final String[] PARLEY_WHITE_LIST_URL = {
//            "/parley/api/auth/login",
//            "/parley/api/auth/signup",
//            "/parley/api/auth/password/reset",
//            "/parley/api/auth/password/reset/**"
//    };
//
//    //   20241216 Adjustments
////    @Autowired
////    private JwtAuthenticationFilter jwtAuthenticationFilter;
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable())
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().authenticated()
//                );
//
//        http.headers(headers -> headers.frameOptions(frameOption -> frameOption.sameOrigin()));
//
//        http.authenticationProvider(authenticationProvider());
//
//        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http
////                .csrf(csrf -> csrf.disable())
////                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
////                .formLogin(form -> form
////                        .loginPage("/parley/api/swagger-ui/index.html")
////                        .permitAll()
////                )
////                .exceptionHandling(exceptions -> exceptions
////                        .defaultAuthenticationEntryPointFor(
////                                (request, response, authException) -> {
////                                    response.sendRedirect("/parley/api/swagger-ui/index.html");
////                                },
////                                new AntPathRequestMatcher("/parley/api/**")
////                        )
////                )
////                .sessionManagement(session ->
////                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .authorizeHttpRequests(auth -> auth
////                        .requestMatchers(TOMCAT_WHITE_LIST_URL).permitAll()
////                        .requestMatchers(SWAGGER_WHITE_LIST_URL).permitAll()
////                        .requestMatchers(PARLEY_WHITE_LIST_URL).permitAll()
////                        .anyRequest().authenticated()
////                )
////                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
////        return http.build();
////    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList(
//                "Authorization",
//                "Content-Type",
//                "X-Requested-With",
//                "Accept",
//                "Origin",
//                "Access-Control-Request-Method",
//                "Access-Control-Request-Headers"
//        ));
//        configuration.setExposedHeaders(Arrays.asList(
//                "Access-Control-Allow-Origin",
//                "Access-Control-Allow-Credentials"
//        ));
//        configuration.setAllowCredentials(true);
//        configuration.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//}