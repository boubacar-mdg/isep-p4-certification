package com.isep.certification.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isep.certification.commons.CommonResponse;
import com.isep.certification.users.models.dtos.AddressRequest;
import com.isep.certification.users.models.dtos.AddressResponse;
import com.isep.certification.users.models.dtos.AvatarRequest;
import com.isep.certification.users.models.dtos.AvatarResponse;
import com.isep.certification.users.models.dtos.ChangePasswordRequest;
import com.isep.certification.users.models.dtos.ChangePasswordResponse;
import com.isep.certification.users.models.dtos.FirebaseTokenRequest;
import com.isep.certification.users.models.dtos.SwitchNumberChange;
import com.isep.certification.users.models.dtos.UpdateInfosRequest;
import com.isep.certification.users.models.dtos.UserResponse;
import com.isep.certification.users.services.UserService;

import io.swagger.v3.oas.annotations.Hidden;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Hidden
public class UserController {

    private final UserService userService;

    @PostMapping("/change/password")
    public ResponseEntity<ChangePasswordResponse> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser) {

        return ResponseEntity.ok(userService.changePassword(request, connectedUser));
    }

    @PostMapping("/update")
    public ResponseEntity<CommonResponse> updateInfos(
            @RequestBody UpdateInfosRequest request) {

        return ResponseEntity.ok(userService.updateInfos(request));
    }

    @PostMapping("/firebase/token/save")
    public ResponseEntity<Void> saveCurrentLoggedInUserFirebaseToken(
            @RequestBody FirebaseTokenRequest request) {
        userService.saveFirebaseToken(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/current")
    public ResponseEntity<UserResponse> synchronizeUserData() {
        return ResponseEntity.ok(userService.syncUserData());
    }

    @GetMapping("/check/session")
    public ResponseEntity<UserResponse> isLogged(@RequestHeader(value="Authorization", required = false) String authorizationHeader) {
        return ResponseEntity.ok(userService.isLogged(authorizationHeader));
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressResponse>> getUsersAddresses() {
        return ResponseEntity.ok(userService.getUsersAddresses());
    }

    @PostMapping("/delete")
    public ResponseEntity<CommonResponse> deleteAccount() {
        return ResponseEntity.ok(userService.deleteAccount());
    }

    @PostMapping("/update/address/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Long addressId, @RequestBody AddressRequest addressRequest) {
        return ResponseEntity.ok(userService.updateAddress(addressId, addressRequest));
    }

    @PostMapping("/add/address")
    public ResponseEntity<AddressResponse> addAddress(@RequestBody AddressRequest addressRequest) {
        return ResponseEntity.ok(userService.addAddress(addressRequest));
    }

    @PostMapping("/delete/address/{addressId}")
    public ResponseEntity<CommonResponse> addAddress(@PathVariable Long addressId) {
        return ResponseEntity.ok(userService.deleteAddress(addressId));
    }
    
    @PostMapping("/address/set-default/{addressId}")
    public ResponseEntity<CommonResponse> setDefaultUserAddress(@PathVariable Long addressId) {
        return ResponseEntity.ok(userService.setDefaultUserAddress(addressId));
    }

    @GetMapping("/avatar")
    public ResponseEntity<AvatarResponse> getUserAvatar() {
        return ResponseEntity.ok(userService.getUserAvatar());
    }
    
    @PostMapping("/upload/avatar")
    public ResponseEntity<CommonResponse> uploadUserAvatar(@RequestBody AvatarRequest avatarRequest) {
        return ResponseEntity.ok(userService.uploadUserAvatar(avatarRequest));
    }
    @PostMapping("/switch/number/update")
    public ResponseEntity<CommonResponse> switchNumberUpdate(@RequestBody SwitchNumberChange switchNumberChange) {
        return ResponseEntity.ok(userService.changeUserNumberToPreviousNumber(switchNumberChange.getUserId()));
    }
}
