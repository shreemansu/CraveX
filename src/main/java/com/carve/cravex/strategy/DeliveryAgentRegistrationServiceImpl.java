package com.carve.cravex.strategy;

import com.carve.cravex.dto.AddDeliveryAgentDto;
import com.carve.cravex.dto.BaseUserDto;
import com.carve.cravex.entity.DeliveryAgent;
import com.carve.cravex.entity.User;
import com.carve.cravex.enums.UserRole;
import com.carve.cravex.mappers.RegistrationModelMapper;
import com.carve.cravex.repository.DeliveryAgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryAgentRegistrationServiceImpl implements RoleWiseRegistrationService {

    private final RegistrationModelMapper registrationModelMapper;

    private final DeliveryAgentRepository deliveryAgentRepo;

    @Override
    public UserRole supports() {
        return UserRole.DELIVERY_AGENT;
    }

    @Override
    public void createProfile(BaseUserDto dto, User user) {
        DeliveryAgent deliveryAgent= registrationModelMapper.addDeliveryAgentDtoToDeliveryAgentEntity((AddDeliveryAgentDto)dto,user);
        deliveryAgentRepo.save(deliveryAgent);
    }
}
