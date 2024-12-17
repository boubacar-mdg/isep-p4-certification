package com.isep.certification.email.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isep.certification.system.services.SystemParameterService;

import jakarta.annotation.PostConstruct;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.PreencodedMimeBodyPart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class MailService {

    private final SystemParameterService systemParametersService;

    private String SMTP_SERVER = "";
    private String EMAIL_SENDER = "";
    private String EMAIL_SENDER_PASSWORD = "";
    private int EMAIL_PORT = 587;

    private String EMAIL_SENDER_USER_NAME = "";

    @PostConstruct
    public void init() {

    }

    private static final Logger logger = Logger.getLogger(MailService.class.getName());

    public void send(String subject, String messageContent, String[] addresses, String[] attachements)
            throws Exception {

        SMTP_SERVER = systemParametersService.getParameterValueByCode("SMTP_SERVER","");
        EMAIL_SENDER = systemParametersService.getParameterValueByCode("SMTP_SENDER_EMAIL","");
        EMAIL_SENDER_PASSWORD = systemParametersService.getParameterValueByCode("SMTP_SENDER_PASSWORD","");
        EMAIL_PORT = Integer.parseInt(systemParametersService.getParameterValueByCode("SMTP_PORT",""));
        EMAIL_SENDER_USER_NAME = systemParametersService.getParameterValueByCode("SMTP_SENDER_USER_NAME","");

         Session session = null;

        if(EMAIL_SENDER_PASSWORD!=null && EMAIL_SENDER_PASSWORD.trim().length()>0){
             session = Session.getInstance(getEmailProperties(SMTP_SERVER, EMAIL_PORT), new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_SENDER_USER_NAME, EMAIL_SENDER_PASSWORD);
                }
            });
        }
        else{
            log.info("Without Auth");
            session = Session.getInstance(getEmailProperties(SMTP_SERVER, EMAIL_PORT,"false"));
        }

       

        log.info("EMAIL_SENDER_USER_NAME "+EMAIL_SENDER_USER_NAME);
        log.info("EMAIL_SENDER_PASSWORD "+EMAIL_SENDER_PASSWORD);

        try {
            MimeBodyPart htmlPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            // The message text content ...
            htmlPart.setContent(messageContent, "text/html; charset=utf-8");

            // the message content attachement part
            if (attachements != null && attachements.length != 0)
                multipart = addAttachements(attachements);

            // Set message Body ...
            multipart.addBodyPart(htmlPart);
            // the recipients ....
            final Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", addresses)));
            // The Sender
            message.setFrom(new InternetAddress(EMAIL_SENDER));
            message.setSubject(subject);
            message.setContent(multipart);

            Transport.send(message);
            log.info(
                    "Send mail (" + subject + ") TO: " + Arrays.toString(addresses) + ", From: " + EMAIL_SENDER);
        } catch (final Exception ex) {
            log.error("Erreur lors de l'envoi du mail " + ex.getMessage(), ex);
            throw ex;
        }

    }


    private static Multipart addAttachements(String[] attachements) throws IOException, MessagingException {
        Multipart multipart = new MimeMultipart();

        for (String f : attachements) {
            MimeBodyPart attachmentBodyPart = new PreencodedMimeBodyPart("base64");
            ;
            attachmentBodyPart.setContent(f, "image/png");
            attachmentBodyPart.setFileName("qrcode.png");
            multipart.addBodyPart(attachmentBodyPart);
        }
        return multipart;
    }

    private static Properties getEmailProperties(String host, int port, String auth) {
        final Properties config = new Properties();
        config.put("mail.smtp.auth", auth);
        config.put("mail.smtp.host", host);
        config.put("mail.smtp.port", port);

        if (port == 587) { // TLS
            config.put("mail.smtp.starttls.enable", "true");
        } else { // SSL 465
            config.put("mail.smtp.socketFactory.port", port);
            config.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        return config;
    }

    private static Properties getEmailProperties(String host, int port) {
       return getEmailProperties( host,  port,"true");
    }

}
