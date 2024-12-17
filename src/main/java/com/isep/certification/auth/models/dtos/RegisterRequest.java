package com.isep.certification.auth.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  private String countryCode;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private String password;
}
