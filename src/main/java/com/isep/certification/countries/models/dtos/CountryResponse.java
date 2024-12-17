package com.isep.certification.countries.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryResponse {
    private String code;
    private String iso2;
    private String iso3;
    private String name;
    private String telephoneCode;
}
