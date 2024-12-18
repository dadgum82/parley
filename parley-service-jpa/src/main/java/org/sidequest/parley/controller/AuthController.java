package org.sidequest.parley.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.sidequest.parley.api.AuthApi;
import org.sidequest.parley.model.*;
import org.sidequest.parley.security.JwtTokenUtil;
import org.sidequest.parley.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private static final Logger log = Logger.getLogger(AuthController.class.getName());

    private final AuthenticationService authenticationService;
    private final JwtTokenUtil jwtTokenUtil;
    private final HttpServletRequest request;

    @Override
    public ResponseEntity<AuthResponse> signup(SignupRequest signupRequest) {
        try {
            log.info("Processing signup request for user: " + signupRequest.getUsername());
            AuthResponse response = authenticationService.signup(signupRequest);
            log.info("Signup successful for user: " + signupRequest.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.warning("Signup failed: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.severe("Signup error: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<AuthResponse> authenticate(AuthRequest request) {
        try {
            log.info("Processing authentication request for user: " + request.getUsername());
            AuthResponse response = authenticationService.authenticate(request);
            log.info("Authentication successful for user: " + request.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.warning("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }

    @Override
    public ResponseEntity<AuthResponse> refreshToken() {
        try {
            String token = getTokenFromRequest();
            if (token == null) {
                log.warning("Token refresh failed: No token provided");
                return ResponseEntity.status(401).build();
            }
            AuthResponse response = authenticationService.refreshToken(token);
            log.info("Token refresh successful");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warning("Token refresh failed: " + e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }

    @Override
    public ResponseEntity<Void> logout() {
        SecurityContextHolder.clearContext();
        log.info("User logged out successfully");
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> changePassword(PasswordChangeRequest passwordChangeRequest) {
        try {
            String token = getTokenFromRequest();
            if (token == null) {
                log.warning("Password change failed: No token provided");
                return ResponseEntity.status(401).build();
            }

            String username = jwtTokenUtil.getUsernameFromToken(token);
            authenticationService.changePassword(
                    username,
                    passwordChangeRequest.getCurrentPassword(),
                    passwordChangeRequest.getNewPassword()
            );
            log.info("Password changed successfully for user: " + username);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.warning("Password change failed: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Void> requestPasswordReset(PasswordResetRequest passwordResetRequest) {
        try {
            log.info("Processing password reset request for email: " + passwordResetRequest.getEmail());
            authenticationService.initiatePasswordReset(passwordResetRequest.getEmail());
            log.info("Password reset request processed successfully");
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.warning("Password reset request failed: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Void> resetPassword(String token, NewPasswordRequest request) {
        try {
            log.info("Processing password reset with token");
            authenticationService.resetPassword(token, request.getPassword());
            log.info("Password reset successful");
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.warning("Password reset failed: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    private String getTokenFromRequest() {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}