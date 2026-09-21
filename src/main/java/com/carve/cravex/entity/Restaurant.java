package com.carve.cravex.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false, unique = true)
    private User user;

    @Column(name = "restaurantname", nullable = false)
    private String restaurantName;

    @Column(name = "restaurantphone", nullable = false,unique = true)
    private String restaurantPhone;

    @Column(nullable = false)
    private String address;

    @Column(name = "openingtime", nullable = false)
    private LocalTime openingTime;

    @Column(name = "closingtime", nullable = false)
    private LocalTime closingTime;

    private boolean isOpen=true;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    @Column(nullable = false)
    private String imageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<FoodMenu> foodMenus;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Order> orders;
}
