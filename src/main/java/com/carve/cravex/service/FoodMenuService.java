package com.carve.cravex.service;

import com.carve.cravex.dto.AddFoodMenuDto;

public interface FoodMenuService {
    String addFoodMenuItemsDtoToEntityService(Long restaurantId,AddFoodMenuDto foodMenuDto);
}
