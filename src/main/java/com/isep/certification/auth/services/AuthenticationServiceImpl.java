package com.isep.certification.auth.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.certification.auth.exceptions.AuthErrors;
import com.isep.certification.auth.exceptions.AuthException;
import com.isep.certification.auth.models.dtos.AuthenticationRequest;
import com.isep.certification.auth.models.dtos.AuthenticationResponse;
import com.isep.certification.auth.models.dtos.RegisterRequest;
import com.isep.certification.commons.utils.ApplicationContextUtils;
import com.isep.certification.commons.utils.Tools;
import com.isep.certification.countries.models.entities.Country;
import com.isep.certification.countries.services.CountryService;
import com.isep.certification.system.services.SystemParameterService;
import com.isep.certification.tokens.models.entities.Token;
import com.isep.certification.tokens.models.enums.TokenType;
import com.isep.certification.tokens.repositories.TokenRepository;
import com.isep.certification.tokens.services.JwtService;
import com.isep.certification.users.models.dtos.UserResponse;
import com.isep.certification.users.models.dtos.UserSignupEvent;
import com.isep.certification.users.models.entities.User;
import com.isep.certification.users.models.entities.UserSubscription;
import com.isep.certification.users.models.enums.AccountState;
import com.isep.certification.users.models.enums.Role;
import com.isep.certification.users.models.enums.Subscription;
import com.isep.certification.users.repositories.UserRepository;
import com.isep.certification.users.repositories.UserSubscriptionRepository;
import com.isep.certification.users.services.UserService;
import com.isep.certification.verifications.models.enums.VerificationType;
import com.isep.certification.verifications.services.VerificationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final VerificationService verificationService;
  private final SystemParameterService systemParameterService;
  private final CountryService countryService;
  private final ModelMapper modelMapper;
  private final UserService userService;
  private final UserSubscriptionRepository userSubscriptionRepository;

  @Override
  public AuthenticationResponse register(RegisterRequest request) {

    if (request.getFirstName() == null
        || request.getFirstName() == null
        || request.getPhoneNumber() == null || request.getPassword() == null || request.getCountryCode() == null
        || request.getFirstName().equals("") || request.getFirstName().equals("") || request.getPhoneNumber().equals("")
        || request.getPassword().equals("") || request.getCountryCode().equals("")) {
      throw new AuthException(AuthErrors.EMPTY_FIELDS);
    }

    Country country = countryService.findByCountryCode(request.getCountryCode());

    if (Tools.validatePhoneNumber(request.getPhoneNumber(), country.getPhoneNumberRegexValidator()) == false)
      throw new AuthException(AuthErrors.INVALID_PHONE_NUMBER);
  
    String finalPhoneNumber = country.getTelephoneCode() + request.getPhoneNumber().replace(" ", "");

    Optional<User> queryUser = repository.findByPhoneNumber(finalPhoneNumber);

    if (queryUser.isPresent())
      throw new AuthException(AuthErrors.USER_ALREADY_EXISTS);
  

    String verificationStep = systemParameterService.getParameterValueByCode("REGISTRATION_VERIFICATION_STEP", "n");
    UserSubscription userSubscription = userSubscriptionRepository.findByPrice(0L)
        .orElseThrow(() -> new AuthException(AuthErrors.SUBSCRIPTION_NOT_FOUND));

    User user = User.builder()
        .fullName(request.getFirstName() + " " + request.getLastName())
        .phoneNumber(finalPhoneNumber)
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .address("--")
        .email("--")
        .country(country)
        .createAt(LocalDateTime.now())
        .subscription(userSubscription)
        .accountState(verificationStep.equals("y") ? AccountState.INACTIVE : AccountState.ACTIVE)
        .build();

    User savedUser = repository.save(user);
    String jwtToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(savedUser);
    UserResponse userResponse = modelMapper.map(savedUser, UserResponse.class);
    userResponse.setCountry(savedUser.getCountry().getCode());
    userResponse.setCurrentSubscription(savedUser.getSubscription().getName());

    saveUserToken(savedUser, jwtToken);

    UserSignupEvent event = new UserSignupEvent();
    event.setUser(savedUser);
    ApplicationContextUtils.getApplicationContext().publishEvent(event);

    AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .nextTransition(verificationStep.equals("y") ? "account/activation" : "dashboard/orders")
        .user(userResponse)
        .build();

    if (verificationStep.equals("y")) {
      Long verificationId = verificationService.createVerificationRequest(user.getId(), VerificationType.REGISTRATION);
      authenticationResponse.setVerificationId(verificationId);
    }

    return authenticationResponse;
  }

  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest request) {

    if (request.getPhoneNumber() == null || request.getPassword() == null || request.getCountryCode() == null
        || request.getPhoneNumber().equals("")
        || request.getPassword().equals("")
        || request.getCountryCode().equals("")) {
      throw new AuthException(AuthErrors.EMPTY_FIELDS);
    }

    Country country = countryService.findByCountryCode(request.getCountryCode());
    String finalPhoneNumber = country.getTelephoneCode() + request.getPhoneNumber().replace(" ", "");

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        finalPhoneNumber, request.getPassword());

    try {
      Authentication authentication = authenticationManager.authenticate(authToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      User user = repository.findByPhoneNumber(finalPhoneNumber)
          .orElseThrow();

      if (user.getAccountState() == AccountState.BLOCKED) {
        throw new AuthException(AuthErrors.BLOCKED_ACCOUNT);
      }

      String jwtToken = jwtService.generateToken(user);
      String refreshToken = jwtService.generateRefreshToken(user);
      revokeAllUserTokens(user);
      saveUserToken(user, jwtToken);

      String verificationStep = systemParameterService.getParameterValueByCode("REGISTRATION_VERIFICATION_STEP", "n");

      UserResponse userResponse = modelMapper.map(user, UserResponse.class);
      userResponse.setCountry(user.getCountry().getCode());
      userResponse.setCurrentSubscription(user.getSubscription().getName());

      AuthenticationResponse loggedInUserInformations = AuthenticationResponse.builder()
          .accessToken(jwtToken)
          .refreshToken(refreshToken)
          .nextTransition(user.getAccountState() == AccountState.ACTIVE ? "dashboard/orders" : "account/activation")
          .user(userResponse).build();

      if (user.getAccountState() == AccountState.INACTIVE && verificationStep.equals("y")) {
        Long verificationId = verificationService.createVerificationRequest(user.getId(),
            VerificationType.REGISTRATION);
        loggedInUserInformations.setVerificationId(verificationId);
      }

      return loggedInUserInformations;
    } catch (Exception ex) {
      throw new AuthException(AuthErrors.INVALID_CREDIDENTIALS);
    }
  }

  @Override
  public void saveUserToken(User user, String jwtToken) {
    Token token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  @Override
  public void revokeAllUserTokens(User user) {
    List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  @Override
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
          .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  @Override
  public AuthenticationResponse createSession(String phoneNumber) throws Exception {
    User user = repository.findByPhoneNumber(phoneNumber)
        .orElseThrow();
    String jwtToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);

    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .nextTransition(user.getAccountState() == AccountState.ACTIVE ? "HOME"
            : user.getAccountState() == AccountState.INACTIVE ? "ACTIVATION" : "BLOCKED")
        .build();
  }

}
