package com.leolautens.payment_system.service;

import com.leolautens.payment_system.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;

@Service

public class MailService {

    private JavaMailSender mailSender;

    private String verifyUrl = "http://localhost:8080/api/v1/users/verify?code=";


    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException {
       String toAddress = user.getEmail();
       String fromAddress = "payment-system@gmail.com";
       String senderName = "Payment System";
       String subject = "Please verify your registration";
       String content = "Dear [[name]],<br>" +
               "Please click the link below to verify your registration:<br>" +
               "<h3><a href=\"[[URL]]\">VERIFY</a></h3>" +
               "Thank you,<br>" +
               "Payment System.";

       MimeMessage message = mailSender.createMimeMessage();
       MimeMessageHelper helper = new MimeMessageHelper(message, true);

       helper.setFrom(fromAddress, senderName);
       helper.setTo(toAddress);
       helper.setSubject(subject);

       content = content.replace("[[name]]", user.getName());
       String verifyUrl = this.verifyUrl + user.getVerificationCode();
       content = content.replace("[[URL]]", verifyUrl);

       helper.setText(content, true);

       mailSender.send(message);
    }


}
