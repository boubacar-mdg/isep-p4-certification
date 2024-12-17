package com.isep.certification.users.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isep.certification.users.models.entities.RequestUserNumberChange;


public interface RequestUserNumberChangeRepository extends JpaRepository<RequestUserNumberChange, Long>{
    Optional<RequestUserNumberChange> findByFromNumberAndToNumberAndIsDone(String from, String to, Boolean isDone);
    Optional<RequestUserNumberChange> findByUserIdAndIsDone(Long userId, Boolean isDone);
}
