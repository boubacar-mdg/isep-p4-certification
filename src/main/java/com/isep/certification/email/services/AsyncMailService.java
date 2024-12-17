package com.isep.certification.email.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AsyncMailService {

    private final MailService mailService;

    @Async
    public void sendEmail(String subject, String content, String adress) {
        String[] addresses = new String[1];
        addresses[0] = adress;
        try {
            mailService.send(subject, content, addresses, (String[]) null);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
