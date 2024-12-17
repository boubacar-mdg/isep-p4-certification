package com.isep.certification.countries.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isep.certification.countries.models.entities.Country;

public interface CountryRepository  extends JpaRepository<Country, String>{
    List<Country> findAllByActive(Boolean active);
}
