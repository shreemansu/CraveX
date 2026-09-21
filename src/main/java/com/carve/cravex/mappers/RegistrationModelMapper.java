package com.carve.cravex.mappers;

import com.carve.cravex.dto.AddCustomerDto;
import com.carve.cravex.dto.AddDeliveryAgentDto;
import com.carve.cravex.dto.AddRestaurantDto;
import com.carve.cravex.dto.BaseUserDto;
import com.carve.cravex.entity.Customer;
import com.carve.cravex.entity.DeliveryAgent;
import com.carve.cravex.entity.Restaurant;
import com.carve.cravex.entity.User;
import com.carve.cravex.enums.AccountStatus;
import com.carve.cravex.enums.UserRole;
import com.carve.cravex.util.GeoUtils;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RegistrationModelMapper {

    private final PasswordEncoder encoder;

    public User addUserDtoToUserEntity(BaseUserDto dto, UserRole role){
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .isActive(true)
                .role(role)
                .accountStatus(role==UserRole.CUSTOMER ? AccountStatus.ACTIVE:AccountStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    //CustomerDtoMapper
    public Customer addCustomerDtoToCustomerEntity(AddCustomerDto customerDto, User savedUser){
        Point location= GeoUtils.createPoint(
                customerDto.getLatitude(),
                customerDto.getLongitude()
        );
        return Customer.builder()
                .user(savedUser)
                .address(customerDto.getAddress())
                .location(location)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    //RestaurantDtoMapper
    public Restaurant addRestaurantDtoToRestaurantEntity(AddRestaurantDto dto, User savedUser){
        Point location=GeoUtils.createPoint(
                dto.getLatitude(),
                dto.getLongitude()
        );

        return Restaurant.builder()
                .user(savedUser)
                .restaurantName(dto.getRestaurantName())
                .restaurantPhone(dto.getRestaurantPhone())
                .address(dto.getAddress())
                .imageUrl(dto.getImageUrl())
                .location(location)
                .isOpen(true)
                .openingTime(dto.getOpeningTime())
                .closingTime(dto.getClosingTime())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public DeliveryAgent addDeliveryAgentDtoToDeliveryAgentEntity(AddDeliveryAgentDto dto,User savedUsed){
        Point currentLocation=GeoUtils.createPoint(
                dto.getCurrentLatitude(),
                dto.getCurrentLongitude()
        );
        return DeliveryAgent.builder()
                .user(savedUsed)
                .vehicleNumber(dto.getVehicleNumber())
                .vehicleType(dto.getVehicleType())
                .currentLocation(currentLocation)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
