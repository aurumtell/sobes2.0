package com.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationConfirmationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("dayana.astana@yandex.ru");
        message.setTo(to);
        message.setSubject("Registration Confirmation");
        message.setText("Thank you for registering. Please confirm your registration with this code: " + code);

        mailSender.send(message);
    }
}
