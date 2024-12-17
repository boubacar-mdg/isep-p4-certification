package com.isep.certification.countries.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.isep.certification.auth.exceptions.AuthErrors;
import com.isep.certification.auth.exceptions.AuthException;
import com.isep.certification.countries.models.dtos.CountryResponse;
import com.isep.certification.countries.models.entities.Country;
import com.isep.certification.countries.repositories.CountryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CountryResponse> listCountries() {
        List<CountryResponse> countries = countryRepository.findAllByActive(true).stream()
                .map(data -> modelMapper.map(data, CountryResponse.class)).toList();
        return countries;
    }

    @Override
    public CountryResponse findByCode(String code) {
        return modelMapper.map(
                countryRepository.findById(code).orElseThrow(() -> new AuthException(AuthErrors.COUNTRY_NOT_ALLOWED)),
                CountryResponse.class);
    }

    @Override
    public Country findByCountryCode(String code) {
        Optional<Country> country = countryRepository.findById(code);
        if(!country.isPresent()) throw new AuthException(AuthErrors.COUNTRY_NOT_ALLOWED);
        if(country.get().getActive() == false) throw new AuthException(AuthErrors.COUNTRY_NOT_ALLOWED);
        return countryRepository.findById(code).get();
    }

}
