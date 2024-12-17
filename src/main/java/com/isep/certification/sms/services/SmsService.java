package com.isep.certification.sms.services;

import com.isep.certification.sms.models.entities.Sms;

public interface SmsService {
    public void sendSmsMessage(Sms sms);
}
