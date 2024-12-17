package com.isep.certification.verifications.models.entities;

import java.time.LocalDateTime;

import com.isep.certification.verifications.models.enums.VerificationState;
import com.isep.certification.verifications.models.enums.VerificationType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "verification_requests")
public class VerificationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_verification_requests")
    @SequenceGenerator(name = "seq_verification_requests", sequenceName = "seq_verification_requests", allocationSize = 20, initialValue = 10)
    private Long id;
    private String code;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private VerificationType type;
    @Enumerated(EnumType.STRING)
    private VerificationState state;
    private Boolean valid;
    private LocalDateTime expirationDate;
    private LocalDateTime requestDate;
}
