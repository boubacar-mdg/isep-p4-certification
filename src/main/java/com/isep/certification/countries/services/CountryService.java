package com.isep.certification.countries.services;

import java.util.List;

import com.isep.certification.countries.models.dtos.CountryResponse;
import com.isep.certification.countries.models.entities.Country;

public interface CountryService {
    List<CountryResponse> listCountries();
    CountryResponse findByCode(String code);
    Country findByCountryCode(String code);
}
