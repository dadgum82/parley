package org.sidequest.parley.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sidequest.parley.api.AuthApi;
import org.sidequest.parley.exception.DuplicateResourceException;
import org.sidequest.parley.exception.ValidationException;
import org.sidequest.parley.model.AuthRequest;
import org.sidequest.parley.model.AuthResponse;
import org.sidequest.parley.model.PasswordResetRequest;
import org.sidequest.parley.model.SignupRequest;
import org.sidequest.parley.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class AuthenticationController implements AuthApi {
    private static final Logger log = Logger.getLogger(AuthenticationController.class.getName());

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ResponseEntity<AuthResponse> authenticate(AuthRequest authRequest) {
        try {
            AuthResponse response = authenticationService.authenticate(authRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warning("Bad request: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .header("X-Error-Message", e.getMessage())
                    .header("X-Error-Code", "INVALID_REQUEST")
                    .build();
        } catch (RuntimeException e) {
            log.warning("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401)
                    .header("X-Error-Message", "Invalid credentials")
                    .header("X-Error-Code", "INVALID_CREDENTIALS")
                    .build();
        } catch (Exception e) {
            log.severe("Error during authentication: " + e.getMessage());
            return ResponseEntity.status(500)
                    .header("X-Error-Message", "An internal server error occurred")
                    .header("X-Error-Code", "INTERNAL_ERROR")
                    .build();
        }
    }

    @Override
    public ResponseEntity<AuthResponse> signup(SignupRequest signupRequest) {
        try {
            AuthResponse response = authenticationService.signup(signupRequest);
            return ResponseEntity.status(201).body(response);
        } catch (ValidationException e) {
            log.warning("Validation error during signup: " + e.getMessage());
            String errorJson = "{}";
            try {
                if (e.getErrors() != null) {
                    errorJson = objectMapper.writeValueAsString(e.getErrors());
                }
            } catch (Exception ex) {
                log.severe("Error serializing validation errors: " + ex.getMessage());
            }

            return ResponseEntity.badRequest()
                    .header("X-Error-Message", e.getMessage())
                    .header("X-Error-Code", "VALIDATION_ERROR")
                    .header("X-Validation-Errors", errorJson)
                    .build();
        } catch (DuplicateResourceException e) {
            log.warning("Duplicate resource error during signup: " + e.getMessage());
            return ResponseEntity.status(409)
                    .header("X-Error-Message", e.getMessage())
                    .header("X-Error-Code", "RESOURCE_EXISTS")
                    .build();
        } catch (Exception e) {
            log.severe("Error during signup: " + e.getMessage());
            return ResponseEntity.status(500)
                    .header("X-Error-Message", "An internal server error occurred")
                    .header("X-Error-Code", "INTERNAL_ERROR")
                    .build();
        }
    }

    @Override
    public ResponseEntity<Void> requestPasswordReset(PasswordResetRequest passwordResetRequest) {
        try {
            authenticationService.initiatePasswordReset(passwordResetRequest.getEmail());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.warning("Bad request during password reset: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .header("X-Error-Message", e.getMessage())
                    .header("X-Error-Code", "INVALID_REQUEST")
                    .build();
        } catch (Exception e) {
            log.severe("Error during password reset: " + e.getMessage());
            return ResponseEntity.status(500)
                    .header("X-Error-Message", "An unexpected error occurred")
                    .header("X-Error-Code", "INTERNAL_ERROR")
                    .build();
        }
    }
}