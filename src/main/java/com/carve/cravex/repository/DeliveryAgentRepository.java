package com.carve.cravex.repository;

import com.carve.cravex.entity.DeliveryAgent;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Long> {

    @Query(value = """
       select da.* from delivery_agents da where da.is_available=true
       and da.current_location is not null 
       order by da.current_location <-> cast(:restaurantPoint as geometry)
       limit 1
       """,nativeQuery = true)
    Optional<DeliveryAgent> findNearestAvailableAgent(@Param("restaurantPoint") Point restaurantPoint);


    @Query(value = """
       select da.* from delivery_agents da where da.is_available=true
       and da.current_location is not null 
       and ST_DWithin(
                  da.current_location::geography,
                  cast(:restaurantPoint as geography),
                  :radiusMetres
              )
       order by da.current_location <-> cast(:radiusMetres as geometry)
       """,nativeQuery = true)
    List<DeliveryAgent> findAvailableAgentsNearby(
            @Param("restaurantPoint") Point restaurantPoint,
            @Param("radiusMetres") double radiusMetres
    );

    @Modifying
    @Transactional
    @Query(value = """
       update delivery_agents set current_location=cast(:newLocation as geometry), updated_at=now()
       where id= :agentId
      """,nativeQuery = true)
    void updateAgentLocation(
            @Param("agentId") Long agentId,
            @Param("newLocation") Point newLocation
    );

    List<DeliveryAgent> findByIsAvailableTrue();
}
