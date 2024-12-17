package com.isep.certification.users.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isep.certification.users.models.dtos.UserRequest;
import com.isep.certification.users.models.dtos.UserResponse;
import com.isep.certification.users.models.dtos.UserSwitchStatus;
import com.isep.certification.users.models.entities.UserSubscription;
import com.isep.certification.users.services.UserSubscriptionService;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

import static com.isep.certification.commons.utils.Constants.BASE_MANAGEMENT_MAPPING;

import java.util.List;


@RestController
@RequestMapping(BASE_MANAGEMENT_MAPPING + "/users/subscriptions")
@RequiredArgsConstructor
@Hidden
public class ManagementUserSubscriptionController {

    private final UserSubscriptionService  userSubscriptionService;

    @GetMapping
    public ResponseEntity<List<UserSubscription>> getAvailableSubscriptions() {
        List<UserSubscription> subscriptions = userSubscriptionService.getAvailableSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    @PostMapping("/add/{name}/{price}")
    public ResponseEntity<Void> addUser(@PathVariable String name, @PathVariable Long price) {
        userSubscriptionService.addSubscription(name, price);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{subscriptionId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long subscriptionId) {
        userSubscriptionService.deleteSubscription(subscriptionId);
        return ResponseEntity.ok().build();
    }
    
}
