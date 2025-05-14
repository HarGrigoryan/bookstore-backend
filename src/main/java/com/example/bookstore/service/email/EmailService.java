package com.example.bookstore.service.email;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}