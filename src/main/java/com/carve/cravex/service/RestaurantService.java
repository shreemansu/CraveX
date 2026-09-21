package com.carve.cravex.service;

import com.carve.cravex.response.NearbyRestaurantResponse;

import java.util.List;

public interface RestaurantService {

    List<NearbyRestaurantResponse> findNearbyRestaurants(Long userId, double radiusKm);
}
