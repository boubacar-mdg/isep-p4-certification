package com.isep.certification.countries.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isep.certification.countries.models.dtos.CountryResponse;
import com.isep.certification.countries.services.CountryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import static com.isep.certification.commons.utils.Constants.BASE_MAPPING;

import java.io.UnsupportedEncodingException;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping(BASE_MAPPING+"/countries")
@Tag(name = "Pays", description = "Gestion des pays")
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    @Operation(summary = "Point de terminaison permettant de récupérer la liste des pays")
    public ResponseEntity<List<CountryResponse>> listCountries() throws UnsupportedEncodingException{
        return ResponseEntity.ok(countryService.listCountries());
    }
    @GetMapping("/{code}")
    @Operation(summary = "Point de terminaison permettant de récupérer un pays par son code")
    public ResponseEntity<CountryResponse> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(countryService.findByCode(code));
    }
    
}
