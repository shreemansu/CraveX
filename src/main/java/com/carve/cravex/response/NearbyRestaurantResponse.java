package com.carve.cravex.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NearbyRestaurantResponse {
    private Long restaurantId;
    private String restaurantName;
    private String address;
    private String imageUrl;
    private double distanceKm;
    private long estimatedDeliveryMinutes;
}
