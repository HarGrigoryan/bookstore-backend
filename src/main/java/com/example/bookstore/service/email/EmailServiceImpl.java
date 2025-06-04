package com.example.bookstore.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${bookstore.email}")
    private String email;

    @Override
    public void sendEmail(
        String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendEmailWithAttachment(String emailTo, String subject, String text, File attachment) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        // Set to true to indicate a multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(email);
        helper.setTo(emailTo);
        helper.setSubject(subject);
        helper.setText(text);

        FileSystemResource file = new FileSystemResource(attachment);
        helper.addAttachment(file.getFilename(), file);

        emailSender.send(message);
    }
}