package com.carve.cravex.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class AddCustomerDto extends BaseUserDto{

    @NotBlank(message = "Address can't be empty")
    private String address;

    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
}
