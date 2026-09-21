package com.carve.cravex.strategy;

import com.carve.cravex.dto.AddRestaurantDto;
import com.carve.cravex.dto.BaseUserDto;
import com.carve.cravex.entity.Restaurant;
import com.carve.cravex.entity.User;
import com.carve.cravex.enums.UserRole;
import com.carve.cravex.mappers.RegistrationModelMapper;
import com.carve.cravex.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantRegistrationServiceImpl implements RoleWiseRegistrationService {

    private final RestaurantRepository restaurantRepo;

    private final RegistrationModelMapper customMapper;


    @Override
    public UserRole supports() {
        return UserRole.RESTAURANT;
    }

    @Override
    public void createProfile(BaseUserDto dto, User user) {
        Restaurant restaurant=customMapper.addRestaurantDtoToRestaurantEntity((AddRestaurantDto) dto,user);
        restaurantRepo.save(restaurant);
    }
}
