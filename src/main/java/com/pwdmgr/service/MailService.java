package com.pwdmgr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void sendSignupMail(String to) {
        String body="Signup successful on password manager.";
        SimpleMailMessage mail=new SimpleMailMessage();
        mail.setTo(to);
        mail.setText(body);
        try {
            mailSender.send(mail);
        } catch (MailSendException e) {
            throw new MailSendException("Invalid email address");
        }
        System.out.println("signup successful mail sent");
    }
}
