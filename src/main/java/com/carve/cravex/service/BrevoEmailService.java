package com.carve.cravex.service;

public interface BrevoEmailService {

    void sendEmail(String receiverEmail, String message, String subject);
}
