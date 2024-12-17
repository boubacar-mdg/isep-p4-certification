package com.isep.certification.users.models.dtos;

import com.isep.certification.users.models.enums.AccountState;
import com.isep.certification.users.models.enums.LoginMode;
import com.isep.certification.users.models.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    public String fullName;
    public String phoneNumber;
    public String email;
    public String address;
    public String avatar;
    public AccountState accountState; 
    public Role role;
    public String country;
    public String currentSubscription;
    public LoginMode loginMode;
}
