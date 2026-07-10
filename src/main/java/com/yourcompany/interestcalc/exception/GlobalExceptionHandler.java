package com.yourcompany.interestcalc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Triggered when @Valid fails on a request body (e.g. amount <= 0). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError err : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(err.getField(), err.getDefaultMessage());
        }
        return build(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors);
    }

    /** Triggered when end date <= start date. */
    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidDateRange(InvalidDateRangeException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    /** Triggered when deleting/fetching a record id that doesn't exist. */
    @ExceptionHandler({NoSuchElementException.class,
            org.springframework.dao.EmptyResultDataAccessException.class})
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex) {
        return build(HttpStatus.NOT_FOUND, "Record not found", null);
    }

    /** Catch-all safety net so the client never sees a raw stack trace. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong. Please try again.", null);
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message, Map<String, String> details) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        if (details != null) body.put("fieldErrors", details);
        return ResponseEntity.status(status).body(body);
    }
}
