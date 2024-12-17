package com.isep.certification.users.models.dtos;

import com.isep.certification.users.models.entities.User;

import lombok.Data;

@Data
public class UserSignupEvent {
    private User user;
}
