package com.isep.certification.users.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.isep.certification.countries.models.entities.Country;
import com.isep.certification.users.models.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
  Optional<User> findByPhoneNumber(String phoneNumber);
  Optional<User> findByPhoneNumberAndCountry(String phoneNumber, Country country);
  Optional<User> findByFirebaseToken(String firebaseToken);
}
