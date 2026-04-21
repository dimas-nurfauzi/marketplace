package com.dims.marketplace.controller;

import com.dims.marketplace.dto.ApiResponse;
import com.dims.marketplace.exceptions.BaseException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Object>> handleBase(BaseException ex) {

        ApiResponse<Object> response = new ApiResponse<>(
                ex.getStatus().value(),
                ex.getMessage(),
                null
        );

        return ResponseEntity
                .status(ex.getStatus())
                .body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrity(DataIntegrityViolationException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "Invalid data";

        Throwable cause = ex.getCause();

        if (cause instanceof org.hibernate.exception.ConstraintViolationException) {
            status = HttpStatus.CONFLICT;
            message = "Data already exists or violates unique constraint";
        }
        else if (cause instanceof org.hibernate.PropertyValueException) {
            status = HttpStatus.BAD_REQUEST;
            message = "Required field is missing";
        }

        ApiResponse<Object> response = new ApiResponse<>(
                status.value(),
                message,
                null
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                message,
                null
        );

        return ResponseEntity.badRequest().body(response);
    }
}