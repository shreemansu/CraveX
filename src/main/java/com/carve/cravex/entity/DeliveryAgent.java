package com.carve.cravex.entity;

import com.carve.cravex.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "delivery_agents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;

    @Column(nullable = false)
    private String vehicleNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;

    @Builder.Default
    private boolean isAvailable=false;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point currentLocation;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "deliveryAgent",fetch = FetchType.LAZY)
    private List<Order> deliveries;

    @OneToMany(mappedBy = "deliveryAgent",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OrderTracking> trackingData;
}
