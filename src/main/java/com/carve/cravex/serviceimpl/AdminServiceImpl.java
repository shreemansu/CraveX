package com.carve.cravex.serviceimpl;

import com.carve.cravex.entity.User;
import com.carve.cravex.enums.AccountStatus;
import com.carve.cravex.event.EmailEvent;
import com.carve.cravex.repository.UserRepository;
import com.carve.cravex.service.AdminService;
import com.carve.cravex.util.EmailBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.carve.cravex.config.RabbitMQConfig.EMAIL_EXCHANGE;
import static com.carve.cravex.config.RabbitMQConfig.EMAIL_WELCOME_ROUTING_KEY;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepo;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public String approveUserService(Long userId) {
        User user=userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        if(user.getAccountStatus()== AccountStatus.ACTIVE){
            throw new RuntimeException("Already Active");
        }
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepo.save(user);
        String emailBody= EmailBuilderUtil.welcomeEmailBuilder(user.getFirstName()+" "+user.getLastName());
        EmailEvent emailEvent=EmailEvent.builder()
                .receiverEmail(user.getEmail())
                .subject("Welcome to CraveX – Your Account is Ready!")
                .message(emailBody)
                .build();
        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE,EMAIL_WELCOME_ROUTING_KEY,emailEvent);
        return "User approved!";
    }

    @Override
    public String rejectUserService(Long userId) {
        User user=userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        user.setAccountStatus(AccountStatus.REJECTED);
        return "User rejected";
    }

    @Override
    public List<User> showAllPendingUsers() {
        return List.of();
    }
}
