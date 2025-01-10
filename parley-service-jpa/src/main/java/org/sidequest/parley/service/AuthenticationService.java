package org.sidequest.parley.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.exception.DuplicateResourceException;
import org.sidequest.parley.exception.ValidationException;
import org.sidequest.parley.model.AuthRequest;
import org.sidequest.parley.model.AuthResponse;
import org.sidequest.parley.model.SignupRequest;
import org.sidequest.parley.repository.UserRepository;
import org.sidequest.parley.security.JwtTokenUtil;
import org.sidequest.parley.util.EmailHelper;
import org.sidequest.parley.util.TimeHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailHelper emailHelper;

    @Value("${default.timezone}")
    private String defaultTimezone;

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserEntity user = (UserEntity) authentication.getPrincipal();
        String token = jwtTokenUtil.generateToken(user.getUsername(), request.getRememberMe());

        return new AuthResponse()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(request.getRememberMe() ? 604800 : 86400); // 7 days : 1 day
    }

    public AuthResponse refreshToken(String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserEntity user = userRepository.findByName(username).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );

        if (jwtTokenUtil.validateToken(token)) {
            String newToken = jwtTokenUtil.generateToken(user.getUsername(), false);

            return new AuthResponse()
                    .token(newToken)
                    .tokenType("Bearer")
                    .expiresIn(86400);
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    @Transactional
    public void initiatePasswordReset(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with this email address"));

        String resetToken = UUID.randomUUID().toString();
        OffsetDateTime expiration = OffsetDateTime.now().plusHours(24);

        userRepository.updatePasswordResetToken(email, resetToken, expiration);
        emailHelper.sendPasswordResetEmail(email, resetToken);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        UserEntity user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token"));

        if (user.getPasswordResetTokenExpiration().isBefore(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        user.setMagic(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiration(null);
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(String username, String currentPassword, String newPassword) {
        UserEntity user = userRepository.findByName(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getMagic())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Update password
        user.setMagic(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        TimeHelper timeHelper = new TimeHelper();
        Map<String, String> errors = new HashMap<>();

        // Validate passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            errors.put("password", "Password and confirmation password do not match");
            throw new ValidationException("Password validation failed", errors);
        }

        // Validate username
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            errors.put("username", "Username is required");
        } else if (userRepository.existsByName(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        // Validate email
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            errors.put("email", "Email is required");
        } else if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email address already exists");
        }

        // If we have any validation errors, throw them
        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed", errors);
        }

        // Create new user
        UserEntity user = new UserEntity();
        user.setName(request.getUsername());
        user.setEmail(request.getEmail());
        user.setMagic(passwordEncoder.encode(request.getPassword()));
        user.setTimezone(timeHelper.isTimezone(request.getTimezone()) ? request.getTimezone() : defaultTimezone);

        userRepository.save(user);
        log.info("Created new user: {}", user.getName());

        // Generate JWT token
        String token = jwtTokenUtil.generateToken(user.getUsername(), false);

        return new AuthResponse()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(86400); // 1 day
    }
}