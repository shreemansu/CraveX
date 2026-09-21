package com.carve.cravex.dto;

import com.carve.cravex.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class AddDeliveryAgentDto extends BaseUserDto {

    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$",message = "Enter valid Vehicle Number")
    private String vehicleNumber;

    @NotBlank(message = "Select vehicle type")
    private VehicleType vehicleType;

    @NotNull
    private Double currentLatitude;

    @NotNull
    private Double currentLongitude;
}
