package com.isep.certification.users.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.isep.certification.auth.exceptions.AuthErrors;
import com.isep.certification.auth.exceptions.AuthException;
import com.isep.certification.commons.CommonResponse;
import com.isep.certification.countries.models.entities.Country;
import com.isep.certification.countries.services.CountryService;
import com.isep.certification.tokens.repositories.TokenRepository;
import com.isep.certification.tokens.services.JwtService;
import com.isep.certification.users.models.dtos.AddressRequest;
import com.isep.certification.users.models.dtos.AddressResponse;
import com.isep.certification.users.models.dtos.AvatarRequest;
import com.isep.certification.users.models.dtos.AvatarResponse;
import com.isep.certification.users.models.dtos.ChangePasswordOnRecoveryRequest;
import com.isep.certification.users.models.dtos.ChangePasswordRequest;
import com.isep.certification.users.models.dtos.ChangePasswordResponse;
import com.isep.certification.users.models.dtos.FirebaseTokenRequest;
import com.isep.certification.users.models.dtos.UpdateInfosRequest;
import com.isep.certification.users.models.dtos.UserRequest;
import com.isep.certification.users.models.dtos.UserResponse;
import com.isep.certification.users.models.dtos.UserSwitchStatus;
import com.isep.certification.users.models.entities.Address;
import com.isep.certification.users.models.entities.RequestUserNumberChange;
import com.isep.certification.users.models.entities.User;
import com.isep.certification.users.models.enums.AccountState;
import com.isep.certification.users.models.enums.LoginMode;
import com.isep.certification.users.repositories.AddressRepository;
import com.isep.certification.users.repositories.RequestUserNumberChangeRepository;
import com.isep.certification.users.repositories.UserRepository;
import com.isep.certification.verifications.models.dtos.VerificationResponse;
import com.isep.certification.verifications.models.entities.VerificationRequest;
import com.isep.certification.verifications.models.enums.VerificationState;
import com.isep.certification.verifications.models.enums.VerificationType;
import com.isep.certification.verifications.services.VerificationService;

import jakarta.transaction.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationService verificationService;
    private final AddressRepository addressRepository;
    private final RequestUserNumberChangeRepository requestUserNumberChangeRepository;
    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final CountryService countryService;

    private String getAuthenticatedUserPhoneNumber() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private User getAuthenticatedUser(String phoneString) {
        return userRepository.findByPhoneNumber(phoneString).get();
    }

    @Override
    public ChangePasswordResponse changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AuthException(AuthErrors.INVALID_CURRENT_PASSWORD);
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new AuthException(AuthErrors.PASSWORD_NOT_IDENTICAL);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        return ChangePasswordResponse.builder()
                .success(true)
                .build();
    }

    @Override
    public ChangePasswordResponse changePasswordOnRecovery(ChangePasswordOnRecoveryRequest request) {

        VerificationRequest verificationRequest = verificationService
                .findVerificationRequest(request.getLastVerificationId());
        if (verificationRequest.getValid() == false
                && verificationRequest.getState().equals(VerificationState.COMPLETED)) {

            User user = userRepository.findById(verificationRequest.getUserId())
                    .orElseThrow(() -> new AuthException(AuthErrors.USER_NOT_FOUND));

            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                throw new AuthException(AuthErrors.SAME_PASSWORD);
            }

            if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
                throw new AuthException(AuthErrors.PASSWORD_NOT_IDENTICAL);
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));

            userRepository.save(user);
        } else {
            throw new AuthException(AuthErrors.REQUEST_NOT_VALIDATED);
        }

        return ChangePasswordResponse.builder()
                .success(true)
                .build();
    }

    @Override
    public ChangePasswordResponse initPasswordRecovery(String phoneNumber, String countryCode) {

        Country country = countryService.findByCountryCode(countryCode);
        User user = userRepository.findByPhoneNumber(country.getTelephoneCode() + phoneNumber)
                .orElseThrow(() -> new AuthException(AuthErrors.USER_NOT_FOUND));

        Long verifId = verificationService.createVerificationRequest(user.getId(), VerificationType.PASSWORD_RECOVERY);

        return ChangePasswordResponse.builder()
                .success(true)
                .verificationId(verifId)
                .build();
    }

    @Override
    public UserResponse syncUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        log.info("Mise à jour du profil de: " + currentPrincipalName);
        User user = userRepository.findByPhoneNumber(currentPrincipalName).get();
        user.setLastSyncroTime(LocalDateTime.now());
        userRepository.save(user);
        return UserResponse.builder()
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .address(user.getAddress())
                .avatar(user.getAvatar())
                .build();
    }

    public User getCurrentUser(String user) {
        return userRepository.findByPhoneNumber(user).get();
    }

    @Override
    public VerificationResponse confirmPasswordVerificationRequest(String code, Long verificationId) {
        VerificationRequest verificationRequest = verificationService.findVerificationRequest(verificationId);
        User user = userRepository.findById(verificationRequest.getUserId())
                .orElseThrow(() -> new AuthException(AuthErrors.USER_NOT_FOUND));
        return verificationService.confirmVerificationRequest(code, verificationRequest.getId(), user.getId(),
                VerificationType.PASSWORD_RECOVERY);
    }

    @Override
    public VerificationResponse confirmAccountActivationVerificationRequest(String code, Long verificationId) {
        VerificationRequest verificationRequest = verificationService.findVerificationRequest(verificationId);
        User user = userRepository.findById(verificationRequest.getUserId())
                .orElseThrow(() -> new AuthException(AuthErrors.USER_NOT_FOUND));
        VerificationResponse verificationResponse = verificationService.confirmVerificationRequest(code, verificationId,
                user.getId(),
                VerificationType.REGISTRATION);
        if (verificationResponse.getSuccess()) {
            user.setAccountState(AccountState.ACTIVE);
            userRepository.save(user);
        }
        return verificationResponse;
    }

    @Override
    public VerificationResponse confirmAccountActivationVerificationRequestNumberChange(String code,
            Long verificationId) {
        VerificationRequest verificationRequest = verificationService.findVerificationRequest(verificationId);
        User user = userRepository.findById(verificationRequest.getUserId())
                .orElseThrow(() -> new AuthException(AuthErrors.USER_NOT_FOUND));
        VerificationResponse verificationResponse = verificationService.confirmVerificationRequest(code, verificationId,
                user.getId(),
                VerificationType.PHONE_NUMBER_CHANGE);

        if (verificationResponse.getSuccess()) {
            Optional<RequestUserNumberChange> requestUserNumberChange = requestUserNumberChangeRepository
                    .findByUserIdAndIsDone(user.getId(), false);
            if (requestUserNumberChange.isPresent()) {
                user.setAccountState(AccountState.ACTIVE);
                requestUserNumberChange.get().setIsDone(true);
                userRepository.save(user);
                requestUserNumberChangeRepository.save(requestUserNumberChange.get());
            } else {
                user.setAccountState(AccountState.ACTIVE);
                userRepository.save(user);
            }
        }

        return verificationResponse;
    }

    @Override
    public Long resendVerificationCode(String countryCode, String phoneNumber) {
        Country country = countryService.findByCountryCode(countryCode);
        User user = userRepository.findByPhoneNumber(country.getTelephoneCode() + phoneNumber)
                .orElseThrow(() -> new AuthException(AuthErrors.USER_NOT_FOUND));
        if (user.getAccountState() == AccountState.ACTIVE)
            new AuthException(AuthErrors.ACCOUNT_ALREADY_ACTIVE);
        Long verificationId = verificationService.createVerificationRequest(user.getId(),
                VerificationType.REGISTRATION);
        return verificationId;
    }

    @Override
    public Long resendVerificationCodeNumberChange(String countryCode, String phoneNumber) {
        Country country = countryService.findByCountryCode(countryCode);
        User user = userRepository.findByPhoneNumber(country.getTelephoneCode() + phoneNumber)
                .orElseThrow(() -> new AuthException(AuthErrors.USER_NOT_FOUND));
        Long verificationId = verificationService.createVerificationRequest(user.getId(),
                VerificationType.REGISTRATION);
        return verificationId;
    }

    @Override
    public CommonResponse updateInfos(UpdateInfosRequest updateInfosRequest) {
        User user = userRepository.findByPhoneNumber(getAuthenticatedUserPhoneNumber()).get();
        Optional<Address> foundAddress = addressRepository.findByAddressAndUserId(updateInfosRequest.getAddress(),
                user.getId());
        log.info("User " + getAuthenticatedUserPhoneNumber() + " is updating his infos "
                + updateInfosRequest.getPhoneNumber());
        if (getAuthenticatedUserPhoneNumber().equals(updateInfosRequest.getPhoneNumber())) {
            log.info("Same phone number as before: " + updateInfosRequest.getPhoneNumber());
            user.setAddress(updateInfosRequest.getAddress());
            if (!foundAddress.isPresent())
                addressRepository
                        .save(Address.builder().address(updateInfosRequest.getAddress()).userId(user.getId())
                                .insertDate(LocalDateTime.now()).isDefault(true).build());
            userRepository.save(user);
        } else {
            if (userRepository.findByPhoneNumber(updateInfosRequest.getPhoneNumber()).isPresent()) {
                return CommonResponse.builder()
                        .message("Ce numéro de téléphone est déjà attribué")
                        .success(true)
                        .build();
            }

            Optional<RequestUserNumberChange> requestUserNumberChange = requestUserNumberChangeRepository
                    .findByFromNumberAndToNumberAndIsDone(getAuthenticatedUserPhoneNumber(),
                            updateInfosRequest.getPhoneNumber(), true);

            if (!requestUserNumberChange.isPresent()) {
                RequestUserNumberChange rnc = RequestUserNumberChange.builder()
                        .userId(user.getId())
                        .fromNumber(getAuthenticatedUserPhoneNumber())
                        .toNumber(updateInfosRequest.getPhoneNumber())
                        .changDateTime(LocalDateTime.now())
                        .isDone(false)
                        .build();
                requestUserNumberChangeRepository.save(rnc);
            } else {
                requestUserNumberChange.get().setIsDone(false);
                requestUserNumberChange.get().setChangDateTime(LocalDateTime.now());
                requestUserNumberChangeRepository.save(requestUserNumberChange.get());
            }

            user.setPhoneNumber(updateInfosRequest.getPhoneNumber());
            user.setAccountState(AccountState.INACTIVE);
            user.setAddress(updateInfosRequest.getAddress());

            if (!foundAddress.isPresent())
                addressRepository
                        .save(Address.builder().address(updateInfosRequest.getAddress()).userId(user.getId())
                                .insertDate(LocalDateTime.now()).isDefault(true).build());
            userRepository.save(user);

            executeAfterTransactionCommits(() -> {
                verificationService.createVerificationRequest(user.getId(), VerificationType.PHONE_NUMBER_CHANGE);
            });

            return CommonResponse.builder()
                    .success(true)
                    .nextTransition("ACTIVATION")
                    .build();

        }

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .build();

        return response;
    }

    @Override
    public CommonResponse deleteAccount() {
        User user = userRepository.findByPhoneNumber(getAuthenticatedUserPhoneNumber()).get();
        user.setAccountState(AccountState.DELETED);
        user.setPhoneNumber("DELETED_" + user.getPhoneNumber());
        userRepository.save(user);
        return CommonResponse.builder()
                .success(true)
                .build();
    }

    @Override
    @Transactional
    public void saveFirebaseToken(FirebaseTokenRequest request) {
        User user = userRepository.findByPhoneNumber(getAuthenticatedUserPhoneNumber()).get();
        if (user.getFirebaseToken() == request.getTokenValue())
            return;
        Optional<User> foundUser = userRepository.findByFirebaseToken(request.getTokenValue());
        if (foundUser.isPresent()) {
            foundUser.get().setFirebaseToken("");
            userRepository.save(foundUser.get());
        }
        log.info("Saving firebase token for user " + user.getPhoneNumber());
        user.setFirebaseToken(request.getTokenValue());
        userRepository.save(user);
    }

    @Override
    public List<AddressResponse> getUsersAddresses() {
        List<AddressResponse> addresses = new ArrayList<AddressResponse>();
        User user = userRepository.findByPhoneNumber(getAuthenticatedUserPhoneNumber()).get();
        List<Address> list = addressRepository.findByUserId(user.getId());
        for (Address address : list) {
            AddressResponse addressResponse = AddressResponse.builder()
                    .id(address.getId())
                    .address(address.getAddress())
                    .build();
            addresses.add(addressResponse);
        }
        return addresses;
    }

    @Override
    public AddressResponse updateAddress(Long addressId, AddressRequest addressRequest) {
        Optional<Address> address = addressRepository.findById(addressId);
        if (!address.isPresent())
            return new AddressResponse();
        address.get().setAddress(addressRequest.getAddress());
        addressRepository.save(address.get());
        AddressResponse addressResponse = AddressResponse.builder()
                .address(address.get().getAddress())
                .build();
        return addressResponse;
    }

    @Override
    public AddressResponse addAddress(AddressRequest addressRequest) {
        User user = userRepository.findByPhoneNumber(getAuthenticatedUserPhoneNumber()).get();
        Optional<Address> foundAddress = addressRepository.findByAddressAndUserId(addressRequest.getAddress(),
                user.getId());
        if (!foundAddress.isPresent())
            addressRepository.save(Address.builder()
                    .address(addressRequest.getAddress())
                    .userId(user.getId())
                    .insertDate(LocalDateTime.now())
                    .isDefault(false)
                    .build());

        AddressResponse addressResponse = AddressResponse.builder()
                .address(addressRequest.getAddress())
                .build();
        return addressResponse;
    }

    @Override
    public CommonResponse deleteAddress(Long id) {
        Optional<Address> address = addressRepository.findByIdAndUserId(id,
                getAuthenticatedUser(getAuthenticatedUserPhoneNumber()).getId());
        if (!address.isPresent())
            return CommonResponse.builder()
                    .success(false)
                    .build();
        addressRepository.delete(address.get());
        return CommonResponse.builder()
                .success(true)
                .build();
    }

    @Override
    public CommonResponse setDefaultUserAddress(Long addressId) {
        User user = userRepository.findByPhoneNumber(getAuthenticatedUserPhoneNumber()).get();
        Optional<Address> address = addressRepository.findByIdAndUserId(addressId, user.getId());
        if (!address.isPresent())
            return CommonResponse.builder()
                    .success(false)
                    .build();
        user.setAddress(address.get().getAddress());
        address.get().setIsDefault(true);
        userRepository.save(user);
        return CommonResponse.builder()
                .success(true)
                .build();
    }

    @Override
    public CommonResponse uploadUserAvatar(AvatarRequest avatarRequest) {
        User user = getAuthenticatedUser(getAuthenticatedUserPhoneNumber());
        user.setAvatar(avatarRequest.getBase64content());
        userRepository.save(user);
        return CommonResponse.builder()
                .success(true)
                .build();
    }

    @Override
    public AvatarResponse getUserAvatar() {
        User user = getAuthenticatedUser(getAuthenticatedUserPhoneNumber());
        return AvatarResponse.builder().base64content(user.getAvatar()).build();
    }

    private void executeAfterTransactionCommits(Runnable task) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            public void afterCommit() {
                task.run();
            }
        });
    }

    @Transactional
    @Override
    public CommonResponse changeUserNumberToPreviousNumber(Long userId) {
        User user = userRepository.findById(userId).get();
        Optional<RequestUserNumberChange> requestUserNumberChange = requestUserNumberChangeRepository
                .findByUserIdAndIsDone(user.getId(), false);
        if (requestUserNumberChange.isPresent()) {
            user.setPhoneNumber(requestUserNumberChange.get().getFromNumber());
            user.setAccountState(AccountState.ACTIVE);
            requestUserNumberChange.get().setIsDone(true);
            return CommonResponse.builder()
                    .success(true)
                    .build();
        }
        return CommonResponse.builder()
                .success(false)
                .message("Aucune demande de changement de numéro de téléphone n'est en cours pour cet utilisateur")
                .build();
    }

    @Override
    public UserResponse isLogged(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthException(AuthErrors.NO_SESSION_IMPLICIT);
        }
        String jwt = authorizationHeader.substring(7);
        var phoneNumber = jwtService.extractUsername(jwt);
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AuthException(AuthErrors.USER_NOT_FOUND));

        if (user.getAccountState().equals(AccountState.INACTIVE))
            throw new AuthException(AuthErrors.INACTIVE_ACCOUNT);

        if (user.getAccountState().equals(AccountState.BLOCKED))
            throw new AuthException(AuthErrors.BLOCKED_ACCOUNT);

        var isTokenValid = tokenRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);

        if (!isTokenValid)
            throw new AuthException(AuthErrors.NO_SESSION_IMPLICIT);

        UserResponse response = modelMapper.map(user, UserResponse.class);
        response.setLoginMode(LoginMode.IMPLICIT);
        response.setCurrentSubscription(user.getSubscription().getName());
        return response;
    }

    @Override
    public void addUser(UserRequest userRequest) {
        User user = User.builder()
                .phoneNumber(userRequest.getPhoneNumber())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(userRequest.getRole())
                .accountState(AccountState.ACTIVE)
                .build();
        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getUsers() {
        List<UserResponse> users = userRepository.findAll().stream()
                .map(data -> modelMapper.map(data, UserResponse.class)).toList();
        return users;
    }

    @Override
    public void switchUserAccountState(UserSwitchStatus userSwitchStatus) {
        User user = userRepository.findById(userSwitchStatus.getUserId())
                .orElseThrow(() -> new AuthException(AuthErrors.USER_NOT_FOUND));
        user.setAccountState(userSwitchStatus.getAccountState());
        userRepository.save(user);
    }

    @Override
    public ChangePasswordResponse resendPasswordRecoveryCode(Long verificationId) {
        VerificationRequest verificationRequest = verificationService.findVerificationRequest(verificationId);
        User user = userRepository.findById(verificationRequest.getUserId())
                .orElseThrow(() -> new AuthException(AuthErrors.USER_NOT_FOUND));
        Long verifId = verificationService.createVerificationRequest(user.getId(),
                VerificationType.PASSWORD_RECOVERY);
        return ChangePasswordResponse.builder()
                .success(true)
                .verificationId(verifId)
                .build();
    }

    @Override
    public CommonResponse deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(AuthErrors.USER_NOT_FOUND));
        userRepository.deleteById(user.getId());
        return CommonResponse.builder()
                .success(true)
                .build();
    }

    @Override
    public User findUserByPhoneNumber(String phoneNumber) {
       return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new AuthException(AuthErrors.USER_NOT_FOUND));
    }

}
