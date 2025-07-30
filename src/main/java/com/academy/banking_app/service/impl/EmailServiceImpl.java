package com.academy.banking_app.service.impl;

import com.academy.banking_app.dto.EmailDetails;
import com.academy.banking_app.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String senderEmail ;


    @Override
    public void sendEmail(EmailDetails emailDetails) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMsgBody());
            mailMessage.setSubject(emailDetails.getSubject());
            javaMailSender.send(mailMessage);
            System.out.println("Mail sent successfully to " + emailDetails.getRecipient());

        }
        catch (MailException e) {
            System.err.println("Error while sending email: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }


    }

}
