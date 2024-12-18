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
            final String authHeader = request.getHeader("Authorization");

            // If no auth header or not a Bearer token, continue with the filter chain
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extract token (remove "Bearer " prefix)
            final String jwt = authHeader.substring(7);
            final String username = jwtUtil.getUsernameFromToken(jwt);

            // If we have a username and no authentication is set yet
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // If token is valid, set up Spring Security context
                if (jwtUtil.validateToken(jwt)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Set authentication for user: {}", username);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        boolean result = false;
        String fullPath = request.getRequestURI();
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        // Remove context path if present
        String path = fullPath.replace(contextPath, "");

        log.debug("Original Path: {}", fullPath);
        log.debug("Processed Path: {}", path);
        log.debug("ServletPath: {}", path);
        log.debug("RequestURI: {}", requestURI);
        log.debug("ContextPath: {}", contextPath);
        log.debug("=== RESULT ===");
        log.debug("path: {}", path);

        if (path.contains("/swagger-ui")
                || path.contains("/api-docs")
                || path.contains("/auth/")
                || path.contains("META-INF")
                || path.contains("/auth/signup")
                || path.contains("/auth/login")
                || path.contains("/auth/logout")
                || path.contains("/parley/auth/signup")
                || path.contains("/parley/auth/login")
                || path.contains("/parley/auth/logout")
                || path.contains("/auth/password/reset")
        ) {
            log.debug("=== This an unprotected endpoint ===");
            result = true;
        } else {
            log.debug("PROTECTED ENDPOINT");
        }

        return result;
    }
}