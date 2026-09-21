package com.carve.cravex.serviceimpl;

import com.carve.cravex.service.BrevoEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class BrevoEmailServiceImpl implements BrevoEmailService {

    private final String apiKey;

    private final String senderEmail;

    private final String senderName;

    private final RestClient restClient;

    private static final String BREVO_API_URL="https://api.brevo.com/v3/smtp/email";


    public BrevoEmailServiceImpl(@Value("${brevo.api.key}")String apiKey,
                                 @Value("${brevo.sender.email}") String senderEmail,
                                 @Value("${brevo.sender.name}") String senderName,
                                 RestClient restClient) {
        this.apiKey = apiKey;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.restClient = restClient;
    }

    @Override
    public void sendEmail(String receiverEmail, String message, String subject) {
        Map<String, Object> requestBody=Map.of("sender",Map.of("email",senderEmail,
                                                                   "name", senderName),
                                                "to", List.of(Map.of("email", receiverEmail)),
                                                "subject", subject,
                                                "htmlContent", message
        );

        restClient.post()
                .uri(BREVO_API_URL)
                .header("api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();
    }
}
