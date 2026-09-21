package com.carve.cravex.strategy;

import com.carve.cravex.dto.BaseUserDto;
import com.carve.cravex.entity.User;
import com.carve.cravex.enums.UserRole;

public interface RoleWiseRegistrationService {
    UserRole supports();
    void createProfile(BaseUserDto dto,User user);
}
