package org.sidequest.parley.handler;

import org.sidequest.parley.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String message = messageSource.getMessage(error.getDefaultMessage(), null, error.getDefaultMessage(), Locale.getDefault());
            errors.put(fieldName, message);
        });

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        // Create error response
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("code", ex.getMessage());
        errorDetails.put("message", messageSource.getMessage(ex.getMessage(), null, ex.getMessage(), Locale.getDefault()));

        body.put("error", errorDetails);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}