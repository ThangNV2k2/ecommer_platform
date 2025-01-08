package com.doan.backend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Service
public class EmailService {
    JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String subject, String emailContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setFrom("thangnv24062002@gmail.com");

            helper.setText(emailContent, true);
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email", e);
        }
    }
}
