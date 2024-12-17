package com.isep.certification.verifications.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isep.certification.verifications.models.entities.VerificationRequest;
import com.isep.certification.verifications.models.enums.VerificationType;


public interface VerificationRequestRepository extends JpaRepository<VerificationRequest, Long> {
        VerificationRequest findFirstByCodeAndUserIdAndValidAndType(String code, Long userId, Boolean valid, VerificationType type);
        VerificationRequest findFirstByIdAndCodeAndUserIdAndValidAndType(Long verificationId, String code, Long userId, Boolean valid, VerificationType type);
        VerificationRequest findByUserIdAndValidAndType(Long userId, Boolean valid, VerificationType type);
        List<VerificationRequest> findAllByUserIdAndValidAndType(Long userId, Boolean valid, VerificationType type);
}
