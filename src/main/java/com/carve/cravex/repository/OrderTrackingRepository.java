package com.carve.cravex.repository;

import com.carve.cravex.entity.OrderTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderTrackingRepository extends JpaRepository<OrderTracking,Long> {

    @Query(value = """
       select ot.* from order_tracking ot 
       where ot.order_id=:orderId
       order by ot.timestamp  
       """,nativeQuery = true)
    List<OrderTracking> findDeliveryPath(
            @Param("orderId") Long orderId
    );

    @Query(value = """
       select ot.* from order_tracking ot
       where ot.order_id=:orderId
       order by ot.timestamp desc 
       limit 1
       """,nativeQuery = true)
    Optional<OrderTracking> findLatestLocation(
            @Param("orderId") Long orderId
    );

    @Query(value = """
       select ST_Distance(
              ot.location::geography,
              o.delivery_location::geography
              ) as remaining_metres
       from order_tracking ot
       JOIN orders o on o.id=ot.order_id
       where ot.order_id=:orderId
       order by ot.timestamp desc 
       limit 1
       """,nativeQuery = true)
    Optional<Double> findRemainingDistanceMetres(
            @Param("orderId") Long orderId
    );
}
