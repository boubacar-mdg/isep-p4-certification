package com.isep.certification.users.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.isep.certification.users.models.entities.UserSubscription;
import com.isep.certification.users.repositories.UserSubscriptionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;

    @Override
    public void addSubscription(String name, Long price) {
        UserSubscription userSubscription = UserSubscription.builder()
                .name(name)
                .price(price)
                .active(true)
                .build();
        userSubscriptionRepository.save(userSubscription);
    }

    @Override
    public List<UserSubscription> getAvailableSubscriptions() {
        return userSubscriptionRepository.findAll();
    }

    @Override
    public void deleteSubscription(Long id) {
        userSubscriptionRepository.deleteById(id);
    }
    
}
