package com.carve.cravex.serviceimpl;

import com.carve.cravex.dispatcher.RoleWiseRegistrationDispatcher;
import com.carve.cravex.dto.BaseUserDto;
import com.carve.cravex.dto.EmailOtpVerifyDto;
import com.carve.cravex.dto.RegistrationTempUserDto;
import com.carve.cravex.entity.User;
import com.carve.cravex.enums.UserRole;
import com.carve.cravex.event.EmailEvent;
import com.carve.cravex.mappers.RegistrationModelMapper;
import com.carve.cravex.repository.UserRepository;
import com.carve.cravex.service.UserRegistrationService;
import com.carve.cravex.util.EmailBuilderUtil;
import com.carve.cravex.util.OtpGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.carve.cravex.config.RabbitMQConfig.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepo;

    private final RegistrationModelMapper customMapper;

    private final ObjectMapper objectMapper;

    private final RoleWiseRegistrationDispatcher roleWiseRegistrationDispatcher;

    private final RabbitTemplate rabbitTemplate;

    private final RedisTemplate<String,String> redisTemplate;

    private static final int OTP_VALIDITY_MINUTES=3;
    private static final int RESEND_LIMIT_HOURS=1;
    private static final int MAX_RESEND_ATTEMPTS=3;

    @Override
    public String initiateUserRegistrationService(BaseUserDto dto, UserRole role) {
        if(userRepo.existsByEmail(dto.getEmail())){
            throw new RuntimeException("User already exists, Go to login");
        }

        String otp= OtpGenerator.generateOtp(6);

//      *** String otp and email in Redis for verification
        RegistrationTempUserDto tempData= RegistrationTempUserDto.builder()
                .tempOtp(otp)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .role(role)
                .baseUserDto(dto)
                .build();
        String key=dto.getEmail();
        try {
            String jsonData=objectMapper.writeValueAsString(tempData);
            redisTemplate.opsForValue().set(key,jsonData,Duration.ofMinutes(OTP_VALIDITY_MINUTES));
            log.info("Temp data saved in redis {}", key);
            log.info(jsonData);
        }catch (Exception e){
            log.error("failed to store data in redis {}",e.getMessage());
            throw new RuntimeException(e.getMessage()+"Failed to store temp user data in redis ");
        }

//      *** Invoking OtpEmailListener to send email
        String emailBody=EmailBuilderUtil.otpEmailTemplateBuilder(dto.getFirstName(), otp);
        EmailEvent event=EmailEvent.builder()
                .receiverEmail(dto.getEmail())
                .message(emailBody)
                .subject("Verify your email")
                .build();
        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE,EMAIL_OTP_ROUTING_KEY,event);

        return "Otp sent Successfully to "+dto.getEmail()+" and it it valid for 5 minutes";
    }

    @Override
    @Transactional
    public String finalUserRegistrationService(EmailOtpVerifyDto otpVerifyDto) {
        String key=otpVerifyDto.getEmail();
        if(!redisTemplate.hasKey(key)) throw new RuntimeException("invalid email");

        RegistrationTempUserDto tempUserData;
        try {
            String jsonData=redisTemplate.opsForValue().get(key);
            if(jsonData==null) throw new RuntimeException("Data in redis is corrupted or deleted");
            jsonData=jsonData.trim();
            tempUserData=objectMapper.readValue(jsonData,RegistrationTempUserDto.class);
            log.info("Okay");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage()+"failed to get data");
        }
        if(!tempUserData.getTempOtp().equals(otpVerifyDto.getOtp())){
            throw new RuntimeException("Wrong Otp Entered");
        }

        if(LocalDateTime.now().isAfter(tempUserData.getExpiryTime())){
            redisTemplate.delete(key);
            throw new RuntimeException("Redis key deleted");
        }

        BaseUserDto baseUserDto=tempUserData.getBaseUserDto();
        UserRole role=tempUserData.getRole();

        User user=customMapper.addUserDtoToUserEntity(baseUserDto,role);
        User savedUser=userRepo.save(user);

        roleWiseRegistrationDispatcher.completeRegistration(role,savedUser,baseUserDto);

        log.info("User saved to entity as {}",user.getEmail());
        redisTemplate.delete(key);
        if(user.getRole()==UserRole.CUSTOMER){
            String emailBody=EmailBuilderUtil.welcomeEmailBuilder(otpVerifyDto.getName());
            EmailEvent event=EmailEvent.builder()
                    .receiverEmail(otpVerifyDto.getEmail())
                    .message(emailBody)
                    .subject("Welcome to CraveX – Your Account is Ready!")
                    .build();
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE,EMAIL_WELCOME_ROUTING_KEY,event);
            return "Registered Successfully";
        }
        return "Registered Successfully! Wait for approval";
    }

    @Override
    @Transactional
    public String deleteUserByIdService(Long id) {
        Optional<User> optUser=userRepo.findById(id);
        if(optUser.isEmpty()) throw new RuntimeException("Invalid User");
        User user=optUser.get();
        user.setActive(false);
        userRepo.save(user);
        log.info("User deleted with id {}",id);
        return "User deleted";
    }

}
