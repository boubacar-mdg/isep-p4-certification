package com.isep.certification.verifications.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.isep.certification.auth.exceptions.AuthErrors;
import com.isep.certification.auth.exceptions.AuthException;
import com.isep.certification.commons.utils.FormatToFrenchDate;
import com.isep.certification.sms.models.entities.Sms;
import com.isep.certification.sms.models.enums.SmsType;
import com.isep.certification.sms.services.SmsService;
import com.isep.certification.system.services.SystemParameterService;
import com.isep.certification.users.models.entities.User;
import com.isep.certification.users.repositories.UserRepository;
import com.isep.certification.verifications.models.dtos.VerificationResponse;
import com.isep.certification.verifications.models.entities.VerificationRequest;
import com.isep.certification.verifications.models.enums.VerificationState;
import com.isep.certification.verifications.models.enums.VerificationType;
import com.isep.certification.verifications.repositories.VerificationRequestRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VerificationServiceImpl implements VerificationService {

    private final VerificationRequestRepository verificationRequestRepository;
    private final UserRepository userRepository;
    private final SmsService smsService;
    private final SystemParameterService systemParameterService;
    private int validityInMinutes = 5;

    @Override
    public Long createVerificationRequest(Long userId, VerificationType type) {

        VerificationRequest vr = verificationRequestRepository.findByUserIdAndValidAndType(userId, true, type);
        Optional<User> user = userRepository.findById(userId);

        // SMS payload
        Sms sms = new Sms();
        sms.setUserNumber(user.get().getPhoneNumber());
        sms.setTitle("Vérification de numéro de téléphone");
        sms.setDate(LocalDateTime.now());
        sms.setSmsType(SmsType.REGISTRATION);

        if (vr != null && vr.getExpirationDate().isAfter(LocalDateTime.now())) {
            log.info("Le numéro de téléphone {} a déjà une demande de vérification en cours pour",
                    user.get().getPhoneNumber());
            log.info("La demande de vérification expire le {}", vr.getExpirationDate());
            log.info("Le code de vérification est {}", vr.getCode());
            sms.setBody("Votre code de verification est " + vr.getCode() + ". Il est valable pour " + validityInMinutes
                    + " minutes et donc expire le " + FormatToFrenchDate.format(vr.getExpirationDate()) + ".");

            if (!systemParameterService.getParameterValueByCode("MODE", "dev").equals("dev"))
                smsService.sendSmsMessage(sms);

            return vr.getId();
        }

        revokeAllPreviousVerificationRequests(userId, type);

        String generateRandomSixDigitCode = getRandomNumberString();

        VerificationRequest verificationRequest = new VerificationRequest();

        verificationRequest.setUserId(userId);
        verificationRequest.setCode(generateRandomSixDigitCode);
        verificationRequest.setValid(true);
        verificationRequest.setType(type);
        verificationRequest.setRequestDate(LocalDateTime.now());
        verificationRequest.setState(VerificationState.INITIATED);
        verificationRequest.setExpirationDate(LocalDateTime.now().plusMinutes(validityInMinutes));

        VerificationRequest vr_new = verificationRequestRepository.save(verificationRequest);

        log.info("Le code de vérification de " + user.get().getPhoneNumber() + " est " + generateRandomSixDigitCode);
        sms.setBody("Votre code de verification est " + generateRandomSixDigitCode + ". Il est valable pour "
                + validityInMinutes + " minutes et donc expire le "
                + FormatToFrenchDate.format(verificationRequest.getExpirationDate()) + ".");

        if (!systemParameterService.getParameterValueByCode("MODE", "dev").equals("dev"))
            smsService.sendSmsMessage(sms);

        return vr_new.getId();

    }

    @Override
    public VerificationResponse confirmVerificationRequest(String code, Long verificationId, Long userId,
            VerificationType type) {
        VerificationRequest verificationRequest = verificationRequestRepository
                .findFirstByIdAndCodeAndUserIdAndValidAndType(verificationId, code, userId, true, type);

        if (verificationRequest == null)
            throw new AuthException(AuthErrors.INVALID_VERIFICATION_CODE);

        if (verificationRequest.getExpirationDate().isBefore(LocalDateTime.now()))
            throw new AuthException(AuthErrors.EXPIRED_VERIFICATION_CODE);

        verificationRequest.setValid(false);
        verificationRequest.setState(VerificationState.COMPLETED);

        verificationRequestRepository.save(verificationRequest);

        return VerificationResponse.builder()
                .success(true)
                .build();
    }

    public String getRandomNumberString() {
        if (systemParameterService.getParameterValueByCode("MODE", "dev").equals("dev"))
            return "000000";
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    public void revokeAllPreviousVerificationRequests(Long userId, VerificationType type) {
        List<VerificationRequest> verificationRequestList = verificationRequestRepository
                .findAllByUserIdAndValidAndType(userId, true, type);

        for (VerificationRequest item : verificationRequestList) {
            item.setValid(false);
            item.setState(VerificationState.CANCELLED);
            verificationRequestRepository.save(item);
        }
    }

    @Override
    public int isVerificationRequestFoundAndValid(Long userId, VerificationType type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isVerificationRequestFoundAndValid'");
    }

    @Override
    public VerificationRequest findVerificationRequest(Long verificationId) {
        return verificationRequestRepository.findById(verificationId)
                .orElseThrow(() -> new AuthException(AuthErrors.VERIFICATION_REQUEST_NOT_FOUND));
    }

}
