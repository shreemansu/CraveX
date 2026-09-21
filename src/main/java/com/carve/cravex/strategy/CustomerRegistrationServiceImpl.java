package com.carve.cravex.strategy;

import com.carve.cravex.dto.AddCustomerDto;
import com.carve.cravex.dto.BaseUserDto;
import com.carve.cravex.entity.Customer;
import com.carve.cravex.entity.User;
import com.carve.cravex.enums.UserRole;
import com.carve.cravex.mappers.RegistrationModelMapper;
import com.carve.cravex.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerRegistrationServiceImpl implements RoleWiseRegistrationService {

    private final RegistrationModelMapper registrationModelMapper;

    private final CustomerRepository customerRepo;

    @Override
    public UserRole supports() {
        return UserRole.CUSTOMER;
    }

    @Override
    public void createProfile(BaseUserDto dto, User user) {
        Customer customer= registrationModelMapper.addCustomerDtoToCustomerEntity((AddCustomerDto)dto,user);
        customerRepo.save(customer);
    }
}
