package com.isep.certification.users.models.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordOnRecoveryRequest {
    private Long lastVerificationId;
    private String newPassword;
    private String confirmationPassword;
}

