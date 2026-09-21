package com.carve.cravex.repository;

import com.carve.cravex.entity.Restaurant;
import com.carve.cravex.projection.NearbyRestaurantProjection;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query(value = """
       select r.* from restaurants r where r.is_open=true
       and ST_DWithin(
              r.location::geography,
              cast(:customerPoint as geography),
              :radiusMetres
            )
       order by r.location <-> cast(:customerPoint as geometry)
       """, nativeQuery = true)
    List<Restaurant> findNearbyOpenRestaurants(
            @Param("customerPoint") Point customerPoint,
            @Param("radiusMetres") double radiusMetres
    );

    @Query(value = """
       select r.id, r.restaurantname, r.address, r.image_url,
              ST_Distance(
              r.location::geography,
              cast(:customerPoint as geography)
              ) as distance_metres
       from restaurants r where r.is_open=true
       and ST_DWithin(
              r.location::geography, 
              cast(:customerPoint as geography),
              :radiusMetres
            )
       order by r.location <-> cast(:customerPoint as geometry)
       """,nativeQuery = true)
    List<NearbyRestaurantProjection> findNearbyWithDistance(
            @Param("customerPoint") Point customerPoint,
            @Param("radiusMetres") double radiusMetres
    );

    List<Restaurant> findByIsOpenTrue();

    @Query(value = """
       select r.* from restaurants r join users u on u.id=r.user_id
       where u.id=:userId 
       and u.account_status='ACTIVE'     
       """,nativeQuery = true)
    Optional<Restaurant> findActiveRestaurantByUserId(@Param("userId") Long userId);
}
