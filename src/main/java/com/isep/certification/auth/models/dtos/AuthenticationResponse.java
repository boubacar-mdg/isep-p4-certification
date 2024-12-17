package com.isep.certification.auth.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isep.certification.users.models.dtos.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {
  @JsonProperty("accessToken")
  private String accessToken;
  @JsonProperty("refreshToken")
  private String refreshToken;
  private String message;
  private String firebaseToken;
  private String nextTransition;
  private UserResponse user;
  private Long verificationId;
}
