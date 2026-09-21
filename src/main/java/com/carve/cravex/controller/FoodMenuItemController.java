package com.carve.cravex.controller;

import com.carve.cravex.dto.AddFoodMenuDto;
import com.carve.cravex.response.ApiResponse;
import com.carve.cravex.service.FoodMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/cravex/restaurant/")
public class FoodMenuItemController {

    private final FoodMenuService foodMenuService;

    @PostMapping("/addItem")
    public ResponseEntity<ApiResponse<String>> addFoodMenuDtoToEntityController(
            Authentication authentication,
            @RequestBody AddFoodMenuDto foodMenuDto){
        Long userId=Long.valueOf(authentication.getName());
        String response=foodMenuService.addFoodMenuItemsDtoToEntityService(userId,foodMenuDto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }
}
