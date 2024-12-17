package com.isep.certification.countries.models.entities;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "countries")
public class Country {
    @Id
    private String code;
    private String iso2;
    private String iso3;
    private String name;
    private String telephoneCode;
    private String phoneNumberRegexValidator;
    private String phoneNumberExample;
    private Boolean active;
}
