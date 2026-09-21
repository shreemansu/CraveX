package com.carve.cravex.service;

import com.carve.cravex.dto.AddCustomerDto;
import com.carve.cravex.dto.BaseUserDto;
import com.carve.cravex.dto.EmailOtpVerifyDto;
import com.carve.cravex.enums.UserRole;

public interface UserRegistrationService {

    String initiateUserRegistrationService(BaseUserDto dto, UserRole role);

    String finalUserRegistrationService(EmailOtpVerifyDto otpVerifyDto);

    String deleteUserByIdService(Long id);
}
