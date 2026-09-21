package com.carve.cravex.dto;

import com.carve.cravex.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationTempUserDto {
    private String tempOtp;
    private LocalDateTime expiryTime;
    private UserRole role;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    private BaseUserDto baseUserDto;
}
