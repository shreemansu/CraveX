package com.carve.cravex.exceptions;

import com.carve.cravex.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> exceptionHandler(Exception e){
        String response=e.getMessage();
        return ResponseEntity.internalServerError().body(ApiResponse.failed(HttpStatus.INTERNAL_SERVER_ERROR,response));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> badCredentialException(BadCredentialsException e){
        String response=e.getLocalizedMessage();
        return ResponseEntity.internalServerError().body(ApiResponse.failed(HttpStatus.UNAUTHORIZED,response));
    }
}
