package com.carve.cravex.serviceimpl;

import com.carve.cravex.response.NearbyRestaurantResponse;
import com.carve.cravex.entity.Customer;
import com.carve.cravex.projection.NearbyRestaurantProjection;
import com.carve.cravex.repository.CustomerRepository;
import com.carve.cravex.repository.RestaurantRepository;
import com.carve.cravex.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepo;

    private final CustomerRepository customerRepo;

    @Override
    public List<NearbyRestaurantResponse> findNearbyRestaurants(Long userId, double radiusKm) {

        Customer customer=customerRepo.findByUserId(userId)
                .orElseThrow(()->new RuntimeException("Customer is not available"));

        Point customerPoint=customer.getLocation();

        if(customerPoint==null) throw new RuntimeException("Customer location is not available");

        double radiusMetres=radiusKm*1000;

        log.info("Finding restaurants within {}km of",radiusKm);

        List<NearbyRestaurantProjection> results=restaurantRepo.findNearbyWithDistance(customerPoint,radiusMetres);

        return results.stream()
                .map(this::buildNearbyDto)
                .collect(Collectors.toList());
    }

    private NearbyRestaurantResponse buildNearbyDto(NearbyRestaurantProjection projection) {
        double distanceKm=Math.round((projection.getDistanceMetres()/1000.0)*10.0)/10.0;
        return NearbyRestaurantResponse.builder()
                .restaurantId(projection.getId())
                .restaurantName(projection.getRestaurantName())
                .address(projection.getAddress())
                .imageUrl(projection.getImageUrl())
                .distanceKm(distanceKm)
                .estimatedDeliveryMinutes(estimateEta(distanceKm))
                .build();
    }

    private long estimateEta(double distanceKm){
        long travelMinutes=Math.round((distanceKm/25.0)*60);
        return travelMinutes+20+5;
    }
}
