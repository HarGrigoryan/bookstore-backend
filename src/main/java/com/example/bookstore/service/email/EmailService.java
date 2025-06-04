package com.example.bookstore.service.email;

import jakarta.mail.MessagingException;

import java.io.File;

public interface EmailService {
    void sendEmail(String to, String subject, String text);

    void sendEmailWithAttachment(String emailTo, String subject, String text, File attachment) throws MessagingException;
}