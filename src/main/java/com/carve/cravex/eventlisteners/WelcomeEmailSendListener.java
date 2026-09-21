package com.carve.cravex.eventlisteners;

import com.carve.cravex.config.RabbitMQConfig;
import com.carve.cravex.event.EmailEvent;
import com.carve.cravex.service.BrevoEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WelcomeEmailSendListener {

    private final BrevoEmailService brevoEmailService;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_WELCOME_QUEUE)
    public void sendWelcomeEmail(EmailEvent event){
        try {
            brevoEmailService.sendEmail(event.getReceiverEmail(), event.getMessage(), event.getSubject());
            System.out.println("Welcome Mail Sent to "+event.getReceiverEmail());
        } catch (Exception e) {
            System.out.println("Otp Email Error: "+e.getMessage());
        }
    }
}
