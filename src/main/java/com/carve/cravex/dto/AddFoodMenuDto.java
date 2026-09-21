package com.carve.cravex.dto;

import com.carve.cravex.enums.FoodType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddFoodMenuDto {
    private String itemName;
    private FoodType foodType;
    private String description;
    private BigDecimal price;
    private String imageUrl;
}
