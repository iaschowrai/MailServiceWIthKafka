package com.iaschowrai.kafka_mailservice.service;

import com.iaschowrai.kafka_mailservice.config.EmailProviderConfig;
import com.iaschowrai.kafka_mailservice.data.EmailMessageDto;
import com.iaschowrai.kafka_mailservice.exception.EmailMessageSendingException;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
class EmailMessageSenderService {

    private final EmailProviderConfig emailProviderConfig;

    // Sends an email using the provided email message DTO
    public void sendEmail(EmailMessageDto emailMessageDto) throws EmailMessageSendingException {
        try {
            Properties prop = new Properties();
            prop.putAll(emailProviderConfig.getProperties());

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailProviderConfig.getAuth().getUsername(), emailProviderConfig.getAuth().getPassword());
                }
            });

            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailMessageDto.getFrom()));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailMessageDto.getTo()));
            message.setSubject(emailMessageDto.getSubject());

            // Create the email content
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(emailMessageDto.getBody(), "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            // Send the email
            message.setContent(multipart);
            Transport.send(message);
        }catch (MessagingException e){
            log.error(e.getMessage(), e);
            throw new EmailMessageSendingException(e.getMessage(), e);
        }
    }
}
