package com.example.shopapi.infrastructure;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendLowStockAlert(String recipient, String productName, int stock) {
        if (mailSender == null) {
            System.out.println("Email service is disabled (no mail server configured).");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipient);
            message.setSubject("Low Stock Alert: " + productName);
            message.setText("The product '" + productName + "' has only " + stock + " units left in stock.");

            mailSender.send(message);
            System.out.println("Low stock email sent to " + recipient);
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }
}