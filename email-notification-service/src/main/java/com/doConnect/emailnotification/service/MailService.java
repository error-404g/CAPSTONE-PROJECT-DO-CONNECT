package com.doConnect.emailnotification.service;

import com.doConnect.emailnotification.dto.MailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender; // your Gmail

    public String sendMail(MailRequest req) {
        try {
            System.out.println("📧 Sending mail: type=" + req.getType()
                + ", to=" + req.getToEmail());

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(sender);
            msg.setTo(req.getToEmail());
            msg.setSubject(req.getSubject());
            msg.setText(req.getMessage());

            mailSender.send(msg);
            System.out.println("✅ mailSender.send() done, type=" + req.getType());
            return "Email sent";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending email: " + e.getMessage();
        }
    }

}
