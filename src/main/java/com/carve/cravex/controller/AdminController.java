package com.carve.cravex.controller;

import com.carve.cravex.response.ApiResponse;
import com.carve.cravex.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v4/cravex/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("approve/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> approveUserController(@PathVariable Long userId){
        String response= adminService.approveUserService(userId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @PostMapping("reject/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> rejectUserController(@PathVariable Long userId){
        String response= adminService.rejectUserService(userId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }
}
