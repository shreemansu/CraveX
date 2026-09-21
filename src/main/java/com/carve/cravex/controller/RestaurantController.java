package com.carve.cravex.controller;

import com.carve.cravex.response.ApiResponse;
import com.carve.cravex.response.NearbyRestaurantResponse;
import com.carve.cravex.service.RestaurantService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/cravex/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<List<NearbyRestaurantResponse>>> getNearbyRestaurants(
            @RequestParam @Positive double radiusKm,
            Authentication authentication
    ){
        Long userId= Long.valueOf(authentication.getName());
        List<NearbyRestaurantResponse> restaurants= restaurantService.findNearbyRestaurants(userId,radiusKm);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,restaurants));
    }
}
