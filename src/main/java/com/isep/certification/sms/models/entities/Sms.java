package com.isep.certification.sms.models.entities;

import java.time.LocalDateTime;

import com.isep.certification.sms.models.enums.SmsType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users_sms")
public class Sms {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_users_sms")
    @SequenceGenerator(name = "seq_users_sms", sequenceName = "seq_users_sms", allocationSize = 20, initialValue = 10)
    private Long id;
    private String title;
    private String body;
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private SmsType smsType;
    private String userNumber;
}
