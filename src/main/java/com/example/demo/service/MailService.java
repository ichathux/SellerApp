package com.example.demo.service;

import com.example.demo.exception.SpringException;
import com.example.demo.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final MailContentBuilder mailContentBuilder;
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(NotificationEmail notificationEmail){
        log.info("sending email to "+notificationEmail.getRecipient() + " body "+notificationEmail.getBody());
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("admsandler20@gmail.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
//            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
            messageHelper.setText(notificationEmail.getBody());
        };
        try {

            mailSender.send(messagePreparator);
            log.info("Activation email sent!!");
        }catch (MailException e){
            throw new SpringException("Exception occured while sending activation email");
        }
    }
}
