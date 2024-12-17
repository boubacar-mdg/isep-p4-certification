package com.isep.certification.auth.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.isep.certification.auth.models.dtos.AuthenticationRequest;
import com.isep.certification.auth.models.dtos.AuthenticationResponse;
import com.isep.certification.auth.models.dtos.RegisterRequest;
import com.isep.certification.users.models.entities.User;



public interface AuthenticationService {
  public AuthenticationResponse register(RegisterRequest request);
  public AuthenticationResponse authenticate(AuthenticationRequest request);
  public void saveUserToken(User user, String jwtToken);
  public void revokeAllUserTokens(User user);
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response) throws IOException;
  public AuthenticationResponse createSession(String phoneNumber) throws Exception;

}
