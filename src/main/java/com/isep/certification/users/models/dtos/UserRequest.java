package com.isep.certification.users.models.dtos;

import com.isep.certification.users.models.enums.Role;

import lombok.Data;

@Data
public class UserRequest {
    private String phoneNumber;
    private String password;
    private String email;
    private Role role;
}
