package com.isep.certification.users.services;

import java.security.Principal;
import java.util.List;

import com.isep.certification.commons.CommonResponse;
import com.isep.certification.users.models.dtos.AddressRequest;
import com.isep.certification.users.models.dtos.AddressResponse;
import com.isep.certification.users.models.dtos.AvatarRequest;
import com.isep.certification.users.models.dtos.AvatarResponse;
import com.isep.certification.users.models.dtos.ChangePasswordOnRecoveryRequest;
import com.isep.certification.users.models.dtos.ChangePasswordRequest;
import com.isep.certification.users.models.dtos.ChangePasswordResponse;
import com.isep.certification.users.models.dtos.FirebaseTokenRequest;
import com.isep.certification.users.models.dtos.RentInfos;
import com.isep.certification.users.models.dtos.UpdateInfosRequest;
import com.isep.certification.users.models.dtos.UserRequest;
import com.isep.certification.users.models.dtos.UserResponse;
import com.isep.certification.users.models.dtos.UserSwitchStatus;
import com.isep.certification.users.models.entities.User;
import com.isep.certification.verifications.models.dtos.VerificationResponse;

public interface UserService {
    public ChangePasswordResponse changePassword(ChangePasswordRequest request, Principal connectedUser);
    public ChangePasswordResponse changePasswordOnRecovery(ChangePasswordOnRecoveryRequest request);
    public ChangePasswordResponse initPasswordRecovery(String phoneNumber, String countryCode);
    public ChangePasswordResponse resendPasswordRecoveryCode(Long verificationId);
    public UserResponse syncUserData();
    public UserResponse isLogged(String authorizationHeader);
    public VerificationResponse confirmPasswordVerificationRequest(String code, Long verifId);            
    public VerificationResponse confirmAccountActivationVerificationRequest(String code, Long verifId);      
    public VerificationResponse confirmAccountActivationVerificationRequestNumberChange(String code, Long verifId);      
    public Long resendVerificationCode(String countryCode, String phoneNumber);     
    public Long resendVerificationCodeNumberChange(String countryCode, String phoneNumber);     
    public void addUser(UserRequest userRequest);     
    public void switchUserAccountState(UserSwitchStatus userSwitchStatus);      
    public List<UserResponse> getUsers();
    public CommonResponse updateInfos(UpdateInfosRequest updateInfosRequest);
    public CommonResponse deleteAccount();
    public CommonResponse deleteUser(Long userId);
    public void saveFirebaseToken(FirebaseTokenRequest request);
    public List<AddressResponse> getUsersAddresses();
    public AddressResponse updateAddress(Long addressId, AddressRequest addressRequest);
    public AddressResponse addAddress(AddressRequest addressRequest);
    public CommonResponse deleteAddress(Long id);
    public CommonResponse setDefaultUserAddress(Long addressId);
    public CommonResponse uploadUserAvatar(AvatarRequest avatarRequest);
    public AvatarResponse getUserAvatar();
    public CommonResponse changeUserNumberToPreviousNumber(Long userId);
    public User findUserByPhoneNumber(String phoneNumber);
}
