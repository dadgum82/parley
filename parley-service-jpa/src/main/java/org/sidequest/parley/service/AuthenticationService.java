package org.sidequest.parley.service;

import org.sidequest.parley.model.AuthRequest;
import org.sidequest.parley.model.AuthResponse;
import org.sidequest.parley.model.SignupRequest;
import org.sidequest.parley.model.User;
import org.sidequest.parley.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Service
public class AuthenticationService {
    private static final Logger log = Logger.getLogger(AuthenticationService.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public AuthResponse authenticate(AuthRequest authRequest) {
        if (authRequest.getUsername() == null || authRequest.getPassword() == null) {
            throw new IllegalArgumentException("Username and password are required");
        }

        // Get user by name
        User user = userService.getUserByName(authRequest.getUsername());
        if (user == null) {
            log.warning("User not found: " + authRequest.getUsername());
            throw new RuntimeException("Invalid credentials");
        }

        // Verify password matches the magic field
        String storedMagic = userService.getUserMagic(user.getId());
        if (!authRequest.getPassword().equals(storedMagic)) {
            log.warning("Invalid password for user: " + authRequest.getUsername());
            throw new RuntimeException("Invalid credentials");
        }

        return createAuthResponse(user.getName());
    }

    @Transactional
    public AuthResponse signup(SignupRequest signupRequest) {
        // Validate request
        if (signupRequest.getUsername() == null || signupRequest.getPassword() == null) {
            throw new IllegalArgumentException("Username and password are required");
        }

        // Check if username already exists
        if (userService.getUserByName(signupRequest.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        // Create the user
        // Note: We'll need to add setMagic to the UserService
        User newUser = userService.createUserWithPassword(
                signupRequest.getUsername(),
                signupRequest.getPassword(),
                signupRequest.getTimezone()
        );

        return createAuthResponse(newUser.getName());
    }

    private AuthResponse createAuthResponse(String username) {
        String token = jwtTokenUtil.generateToken(username);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setTokenType("Bearer");
        response.setExpiresIn(24 * 60 * 60); // 24 hours in seconds

        return response;
    }
}