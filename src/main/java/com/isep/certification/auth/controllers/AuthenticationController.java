package com.isep.certification.auth.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isep.certification.auth.models.dtos.AuthenticationRequest;
import com.isep.certification.auth.models.dtos.AuthenticationResponse;
import com.isep.certification.auth.models.dtos.RegisterRequest;
import com.isep.certification.auth.services.AuthenticationService;
import com.isep.certification.users.models.dtos.ChangePasswordOnRecoveryRequest;
import com.isep.certification.users.models.dtos.ChangePasswordResponse;
import com.isep.certification.users.models.dtos.ResendCodeResponse;
import com.isep.certification.users.services.UserService;
import com.isep.certification.verifications.models.dtos.VerificationResponse;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Les différentes méthodes pour procéder à l'authentification")
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;

    @Operation(summary = "Ce point de terminaison permet d'effectuer l'inscription de l'utilisateur")
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(service.register(registerRequest));
    }

    @Operation(summary = "Ce point de terminaison permet d'effectuer la connexion de l'utilisateur")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(service.authenticate(authenticationRequest));
    }

    @Operation(summary = "Ce point de terminaison permet de confirmer le code OTP dans le cadre de l'activation du compte")
    @PostMapping("/account/activation/confirm/{verificationId}/{code}") 
    public ResponseEntity<VerificationResponse> confirmAccountActivation(@PathVariable String code,
            @PathVariable Long verificationId) {
        return ResponseEntity.ok(userService.confirmAccountActivationVerificationRequest(code, verificationId));
    }

    @Operation(summary = "Ce point de terminaison permet de renvoyer le code OTP pour activation du compte")
    @PostMapping("/account/activation/resend/{countryCode}/{phoneNumber}")
    public ResponseEntity<ResendCodeResponse> resendActivationVerificationCode(@PathVariable String phoneNumber,
            @PathVariable String countryCode) {
        ResendCodeResponse resendCodeResponse = ResendCodeResponse.builder()
                .verificationId(userService.resendVerificationCode(countryCode, phoneNumber)).build();
        return ResponseEntity.ok(resendCodeResponse);
    }

    @Hidden
    @Operation(summary = "Ce point de terminaison permet de gérer le rafraichissement du token")
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }

    @Hidden
    @Operation(summary = "Ce point de terminaison permet d'amorcer le processus de récupération du mot de passe")
    @PostMapping("/forgot/password/init/{countryCode}/{phoneNumber}")
    public ResponseEntity<ChangePasswordResponse> initPasswordRecovery(@PathVariable String phoneNumber,
            @PathVariable String countryCode) {
        return ResponseEntity.ok(userService.initPasswordRecovery(phoneNumber, countryCode));
    }

    @Hidden
    @Operation(summary = "Ce point de terminaison permet de renvoyer le code de récupération du mot de passe")
    @PostMapping("/forgot/password/resend/{verificationId}")
    public ResponseEntity<ChangePasswordResponse> resendPasswordRecoveryCode(@PathVariable Long verificationId) {
        return ResponseEntity.ok(userService.resendPasswordRecoveryCode(verificationId));
    }

    @Hidden
    @Operation(summary = "Ce point de terminaison permet de confirmer le code de récupération du mot de passe")
    @PostMapping("/forgot/password/confirm/{verificationId}/{code}")
    public ResponseEntity<VerificationResponse> confirmPasswordRecoveryCode(@PathVariable String code,
            @PathVariable Long verificationId) {
        return ResponseEntity.ok(userService.confirmPasswordVerificationRequest(code, verificationId));
    }

    @Hidden
    @Operation(summary = "Ce point de terminaison permet de changer le mot de passe oublié")
    @PostMapping("forgot/password/change")
    public ResponseEntity<ChangePasswordResponse> changeForgottenPassword(
            @RequestBody ChangePasswordOnRecoveryRequest changePasswordOnRecovery) {
        return ResponseEntity.ok(userService.changePasswordOnRecovery(changePasswordOnRecovery));
    }

}
