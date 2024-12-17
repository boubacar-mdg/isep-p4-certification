package com.isep.certification.config.services;

import java.util.List;

import com.isep.certification.system.models.dtos.SystemParameterResponseDTO;

public interface ConfigService {
    public List<SystemParameterResponseDTO> getSystemValues();
}
