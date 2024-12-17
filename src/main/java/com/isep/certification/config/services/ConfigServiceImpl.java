package com.isep.certification.config.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.isep.certification.system.models.dtos.SystemParameterResponseDTO;
import com.isep.certification.system.models.entities.SystemParameter;
import com.isep.certification.system.services.SystemParameterService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ConfigServiceImpl implements ConfigService {

    private final SystemParameterService systemParameterService;

    @Override
    public List<SystemParameterResponseDTO> getSystemValues() {
        List<SystemParameterResponseDTO> responseDTOs = new ArrayList<SystemParameterResponseDTO>();
        List<SystemParameter> systemParameters = systemParameterService.getAllSystemParametersForClient().stream()
                .filter((a) -> !a.getCode().startsWith("COTIZEL_")).toList();
        for (SystemParameter systemParameter : systemParameters) {
            responseDTOs.add(SystemParameterResponseDTO.builder()
                    .code(systemParameter.getCode())
                    .value(systemParameter.getValue())
                    .build());
        }
        return responseDTOs;
    }

}
