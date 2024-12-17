package com.isep.certification.users.services;

import java.util.List;

import com.isep.certification.users.models.entities.UserSubscription;

public interface UserSubscriptionService { 
    public void addSubscription(String name, Long price);       
    public List<UserSubscription> getAvailableSubscriptions();
    public void deleteSubscription(Long id);
}
