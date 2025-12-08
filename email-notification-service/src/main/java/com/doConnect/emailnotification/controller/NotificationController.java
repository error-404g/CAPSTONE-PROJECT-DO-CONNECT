package com.doConnect.emailnotification.controller;

import com.doConnect.emailnotification.dto.MailRequest;
import com.doConnect.emailnotification.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private MailService mailService;

    @PostMapping("/email")
    public String sendEmail(@RequestBody MailRequest request) {
        System.out.println("📧 Incoming email request: to=" + request.getToEmail()
            + ", subject=" + request.getSubject());

        try {
            // existing sendMail call
            return mailService.sendMail(request);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

}
