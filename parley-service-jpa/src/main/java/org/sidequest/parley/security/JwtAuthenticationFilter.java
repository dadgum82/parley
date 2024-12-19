package org.sidequest.parley.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // Enhanced request logging at the start
            log.info("\n");
            log.info("════════════════════════════════════════");
            log.info("JWT FILTER - PROCESSING REQUEST");
            log.info("════════════════════════════════════════");
            log.info("Request Method: {}", request.getMethod());
            log.info("Request URL: {}", request.getRequestURL());
            log.info("Request URI: {}", request.getRequestURI());
            log.info("Context Path: {}", request.getContextPath());
            log.info("Servlet Path: {}", request.getServletPath());
            log.info("Query String: {}", request.getQueryString());

            System.err.println("Request URL: " + request.getRequestURL());

            // Log all headers for debugging
            java.util.Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                log.debug("Header {}: {}", headerName, request.getHeader(headerName));
            }

            final String authHeader = request.getHeader("Authorization");
            log.info("Authorization Header: {}",
                    authHeader != null ? authHeader.substring(0, Math.min(authHeader.length(), 20)) + "..." : "null");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.info("No valid Bearer token found - Proceeding with filter chain");
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = authHeader.substring(7);
            final String username = jwtUtil.getUsernameFromToken(jwt);
            log.info("Extracted username from token: {}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                log.info("Retrieved user details for: {}", username);

                if (jwtUtil.validateToken(jwt)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("Successfully authenticated user: {}", username);
                } else {
                    log.warn("Token validation failed for user: {}", username);
                }
            }

            filterChain.doFilter(request, response);
            log.info("Filter chain completed for request: {}", request.getRequestURI());
            log.info("════════════════════════════════════════\n");

        } catch (Exception e) {
            log.error("JWT Authentication error for request {}: {}", request.getRequestURI(), e.getMessage());
            log.debug("Detailed exception: ", e);
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String fullPath = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = fullPath.replace(contextPath, "");

        log.info("\n");
        log.info("╔══════════════════════════════════════");
        log.info("║ JWT FILTER - CHECKING PATH EXCLUSION");
        log.info("╠══════════════════════════════════════");
        log.info("║ Full Path: {}", fullPath);
        log.info("║ Context Path: {}", contextPath);
        log.info("║ Processed Path: {}", path);

        boolean result = path.contains("/swagger-ui")
                || path.contains("/api-docs")
                || path.contains("/auth/")
                || path.contains("META-INF")
                || path.contains("/auth/signup")
                || path.contains("/auth/login")
                || path.contains("/auth/logout")
                || path.contains("/parley/auth/signup")
                || path.contains("/parley/auth/login")
                || path.contains("/parley/auth/logout")
                || path.contains("/auth/password/reset");

        log.info("║ Filter Decision: {} will {} be filtered", path, result ? "NOT" : "");
        log.info("╚══════════════════════════════════════\n");

        return result;
    }
}