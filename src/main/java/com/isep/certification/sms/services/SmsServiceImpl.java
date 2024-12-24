package com.isep.certification.sms.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.isep.certification.sms.models.entities.Sms;
import com.isep.certification.sms.repositories.SmsRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SmsServiceImpl implements SmsService {

    private final SmsRepository smsRepository;
    
    @Async
    @Override
    public void sendSmsMessage(Sms sms) {
        log.info("Sending "+sms.getSmsType()+" SMS to " + sms.getUserNumber()+ " on "+ LocalDateTime.now());
         RestTemplate restTemplate = new RestTemplate();
        // Create authentication header
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("", "");

        // Create query parameters
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("destAddr", sms.getUserNumber());
        params.add("sourceAddr", "EXPRESSO");
        params.add("message", sms.getBody());

        // Create HTTP entity with headers and parameters
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // Make the POST request
        restTemplate.postForEntity("http://smspro.expressotelecom.sn:9080/user/receive_sms.html", request, String.class);

        smsRepository.save(sms);

    }
    
}
