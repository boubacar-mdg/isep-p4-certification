package com.isep.certification.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isep.certification.users.models.dtos.UserRequest;
import com.isep.certification.users.models.dtos.UserResponse;
import com.isep.certification.users.models.dtos.UserSwitchStatus;
import com.isep.certification.users.services.UserService;

import io.swagger.v3.oas.annotations.Hidden;

import static com.isep.certification.commons.utils.Constants.BASE_MANAGEMENT_MAPPING;

import java.util.List;

@RestController
@RequestMapping(BASE_MANAGEMENT_MAPPING + "/users")
@RequiredArgsConstructor
@Hidden
public class ManagementUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserResponse> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/switch/status")
    public ResponseEntity<Void> switchUserAccountState(@RequestBody UserSwitchStatus userSwitchStatus) {
        userService.switchUserAccountState(userSwitchStatus);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody UserRequest userRequest) {
        userService.addUser(userRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

}
