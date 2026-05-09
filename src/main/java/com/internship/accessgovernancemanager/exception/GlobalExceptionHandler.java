package com.internship.accessgovernancemanager.exception;

import com.internship.accessgovernancemanager.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .filter(error -> "username".equals(error.getField()))
            .map(FieldError::getDefaultMessage)
            .findFirst()
            .or(() -> ex.getBindingResult().getFieldErrors().stream()
                .filter(error -> "password".equals(error.getField()))
                .map(FieldError::getDefaultMessage)
                .findFirst())
            .or(() -> ex.getBindingResult().getFieldErrors().stream()
                .filter(error -> "email".equals(error.getField()))
                .map(FieldError::getDefaultMessage)
                .findFirst())
            .or(() -> ex.getBindingResult().getFieldErrors().stream()
                .filter(error -> "role".equals(error.getField()))
                .map(FieldError::getDefaultMessage)
                .findFirst())
            .or(() -> ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst())
            .orElse("Validation failed");
        return ResponseEntity.badRequest().body(ApiResponse.error(message, null));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(ApiResponse.error(ex.getMessage(), null));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage(), null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(500).body(ApiResponse.error("Internal server error", null));
    }
}
