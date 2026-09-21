package com.carve.cravex.controller;

import com.carve.cravex.dto.*;
import com.carve.cravex.enums.UserRole;
import com.carve.cravex.response.ApiResponse;
import com.carve.cravex.service.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/cravex/user")
@RequiredArgsConstructor
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    @PostMapping("/registration/customer")
    public ResponseEntity<ApiResponse<String>> initiateCustomerRegistrationController(@RequestBody @Valid AddCustomerDto dto){
        String response= userRegistrationService.initiateUserRegistrationService(dto, UserRole.CUSTOMER);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }
    @PostMapping("/registration/restaurant")
    public ResponseEntity<ApiResponse<String>> initiateRestaurantRegistrationController(@RequestBody @Valid AddRestaurantDto dto){
        String response= userRegistrationService.initiateUserRegistrationService(dto, UserRole.RESTAURANT);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }
    @PostMapping("/registration/delivery-agent")
    public ResponseEntity<ApiResponse<String>> initiateDeliveryAgentRegistrationController(@RequestBody @Valid AddDeliveryAgentDto dto){
        String response= userRegistrationService.initiateUserRegistrationService(dto, UserRole.DELIVERY_AGENT);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @PostMapping("/verification")
    public ResponseEntity<ApiResponse<String>> finalUserVerificationController(@RequestBody EmailOtpVerifyDto otpVerifyDto){
        String response= userRegistrationService.finalUserRegistrationService(otpVerifyDto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUserByIdController(@PathVariable("id") Long id){
        String response= userRegistrationService.deleteUserByIdService(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }
}
