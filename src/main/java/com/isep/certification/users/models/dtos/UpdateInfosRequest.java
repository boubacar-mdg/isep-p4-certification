package com.isep.certification.users.models.dtos;

import lombok.Data;

@Data
public class UpdateInfosRequest {
    private String phoneNumber;
    private String address;
}
