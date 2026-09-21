package com.carve.cravex.repository;

import com.carve.cravex.entity.Customer;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    @Modifying
    @Transactional
    @Query(value = """
       update customers set location=cast(:newLocation as geometry),
       updated_at=now()
       where id= :customerId
       """,nativeQuery = true)
    void updateCustomerLocation(
            @Param("customerId") Long customerId,
            @Param("newLocation")Point newLocation
    );

    @Query(value = """
       select ST_Distance(
                     c.location::geography,
                     r.location::geography
              ) as distance_metres
       from customers c join orders o on o.customer_id=c.id
                        join restaurants r on r.id=o.restaurant_id where o.id=:orderId
       """,nativeQuery = true)
    Optional<Customer> findDistanceToRestaurant(
            @Param("orderId") Long orderId
    );

    Optional<Customer> findByUserId(Long userId);
}
