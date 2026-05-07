package com.intership.tool.exception;

import com.intership.tool.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {

        return ResponseEntity.status(500)
                .body(new ApiResponse<>(ex.getMessage(), null));
    }
}