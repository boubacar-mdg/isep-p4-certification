package com.isep.certification.users.models.dtos;

import com.google.auto.value.AutoValue.Builder;
import com.isep.certification.users.models.enums.AccountState;

import lombok.Data;

@Data
@Builder
public class UserSwitchStatus {
    private AccountState accountState;
    private Long userId;
}
