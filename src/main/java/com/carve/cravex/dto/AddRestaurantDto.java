package com.carve.cravex.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class AddRestaurantDto extends BaseUserDto{
    //from restaurant entity
    @NotBlank(message = "Enter valid name")
    private String restaurantName;

    @NotBlank(message = "Enter valid phone number")
    private String restaurantPhone;

    @NotBlank(message = "Enter valid address")
    private String address;

    @NotBlank(message = "Provide image")
    private String imageUrl;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openingTime;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closingTime;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
