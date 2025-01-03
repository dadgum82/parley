package org.sidequest.parley.controller;

import org.sidequest.parley.api.AuthApi;
import org.sidequest.parley.exception.ValidationException;
import org.sidequest.parley.model.AuthRequest;
import org.sidequest.parley.model.AuthResponse;
import org.sidequest.parley.model.PasswordResetRequest;
import org.sidequest.parley.model.SignupRequest;
import org.sidequest.parley.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class AuthenticationController implements AuthApi {
    private static final Logger log = Logger.getLogger(AuthenticationController.class.getName());

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public ResponseEntity<AuthResponse> authenticate(AuthRequest authRequest) {
        try {
            AuthResponse response = authenticationService.authenticate(authRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warning("Bad request: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.warning("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).build();
        } catch (Exception e) {
            log.severe("Error during authentication: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    public ResponseEntity<AuthResponse> signup(SignupRequest signupRequest) {
        try {
            // Validate password match
            if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
                Map<String, String> errors = new HashMap<>();
                errors.put("password", "Password and confirmation password do not match. Please ensure both passwords are identical.");
                throw new ValidationException("signup.password.mismatch");
            }

            AuthResponse response = authenticationService.signup(signupRequest);
            return ResponseEntity.status(201).body(response);
        } catch (ValidationException e) {
            log.warning("Validation error during signup: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            log.warning("Bad request: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.severe("Error during signup: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    public ResponseEntity<Void> requestPasswordReset(PasswordResetRequest passwordResetRequest) {
        try {
            authenticationService.initiatePasswordReset(passwordResetRequest.getEmail());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.warning("Bad request: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.severe("Error during password reset: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }


}

