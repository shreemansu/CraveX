package com.carve.cravex.dispatcher;

import com.carve.cravex.dto.BaseUserDto;
import com.carve.cravex.entity.User;
import com.carve.cravex.enums.UserRole;
import com.carve.cravex.strategy.RoleWiseRegistrationService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RoleWiseRegistrationDispatcher {

    private final Map<UserRole, RoleWiseRegistrationService> handlers;

    public RoleWiseRegistrationDispatcher(List<RoleWiseRegistrationService> handlerList) {
        this.handlers = handlerList.stream()
                .collect(Collectors
                        .toMap(RoleWiseRegistrationService::supports,h->h));
    }

    public void completeRegistration(UserRole role, User user, BaseUserDto dto){
        RoleWiseRegistrationService roleRegistrationService=handlers.get(role);
        if(roleRegistrationService==null) {
            throw new IllegalStateException("No such service for "+role);
        }
        roleRegistrationService.createProfile(dto,user);
    }
}
