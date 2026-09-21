package com.carve.cravex.controller;

import com.carve.cravex.response.ApiResponse;
import com.carve.cravex.dto.LoginRequestDto;
import com.carve.cravex.response.LoginResponse;
import com.carve.cravex.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v3/cravex/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginController(@RequestBody LoginRequestDto loginRequestDto){
        LoginResponse loginResponse =authService.loginService(loginRequestDto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,loginResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logoutController(HttpServletRequest request){
        String response=authService.logoutService(request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }
}
