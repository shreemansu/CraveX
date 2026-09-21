package com.carve.cravex.projection;

public interface NearbyRestaurantProjection {
    Long getId();
    String getRestaurantName();
    String getAddress();
    String getImageUrl();
    Double getDistanceMetres();
}
