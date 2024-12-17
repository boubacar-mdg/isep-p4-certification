package com.isep.certification.sms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isep.certification.sms.models.entities.Sms;

public interface SmsRepository extends JpaRepository<Sms, Long> {
    List<Sms> findAllByUserNumber(String userNumber);
}
