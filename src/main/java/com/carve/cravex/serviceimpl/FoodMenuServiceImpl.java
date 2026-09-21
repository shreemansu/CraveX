package com.carve.cravex.serviceimpl;

import com.carve.cravex.dto.AddFoodMenuDto;
import com.carve.cravex.entity.FoodMenu;
import com.carve.cravex.entity.Restaurant;
import com.carve.cravex.repository.FoodMenuRepository;
import com.carve.cravex.repository.RestaurantRepository;
import com.carve.cravex.service.FoodMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FoodMenuServiceImpl implements FoodMenuService {

    private final FoodMenuRepository foodMenuRepo;

    private final RestaurantRepository restaurantRepo;

    @Transactional
    @Override
    public String addFoodMenuItemsDtoToEntityService(Long userId,AddFoodMenuDto foodMenuDto) {
        Restaurant restaurant=restaurantRepo.findActiveRestaurantByUserId(userId)
                .orElseThrow(()->new RuntimeException("Can't find restaurant or restaurant status is not ACTIVE"));

        FoodMenu foodMenus= FoodMenu.builder()
                .restaurant(restaurant)
                .itemName(foodMenuDto.getItemName())
                .foodType(foodMenuDto.getFoodType())
                .description(foodMenuDto.getDescription())
                .price(foodMenuDto.getPrice())
                .imageUrl(foodMenuDto.getImageUrl())
                .isAvailable(true)
                .preparationTime(30)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        foodMenuRepo.save(foodMenus);
        return "Food menu item added successfully";
    }
}
