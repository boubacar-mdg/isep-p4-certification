package com.isep.certification.verifications.services;

import com.isep.certification.verifications.models.dtos.VerificationResponse;
import com.isep.certification.verifications.models.entities.VerificationRequest;
import com.isep.certification.verifications.models.enums.VerificationType;

public interface VerificationService {

    public Long createVerificationRequest(Long userId, VerificationType type);
    public int isVerificationRequestFoundAndValid(Long userId, VerificationType type);
    public VerificationResponse confirmVerificationRequest(String code, Long verificationId, Long userId, VerificationType type);
    public VerificationRequest findVerificationRequest(Long verificationId);
}
