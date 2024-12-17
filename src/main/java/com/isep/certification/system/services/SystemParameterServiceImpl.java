package com.isep.certification.system.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.isep.certification.commons.CommonResponse;
import com.isep.certification.system.models.ParameterScope;
import com.isep.certification.system.models.dtos.UpdateSystemParameterDTO;
import com.isep.certification.system.models.entities.SystemParameter;
import com.isep.certification.system.repositories.SystemParameterRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SystemParameterServiceImpl implements SystemParameterService {

    private final SystemParameterRepository systemParameterRepository;

    @Override
    public String getParameterValueByCode(String code) {
        SystemParameter systemParameter = systemParameterRepository.findByCode(code).get();
        return systemParameter.getValue();
    }

    @Override
    public String getParameterValueByCode(String code, String defaultValue) {
         Optional<SystemParameter> systemParameter = systemParameterRepository.findByCode(code);
         if(!systemParameter.isPresent())
             return defaultValue;
         else
             return systemParameter.get().getValue();
    }

    @Override
    public List<SystemParameter> getAllSystemParametersForClient() {
        return systemParameterRepository.findAllByParameterScope(ParameterScope.CLIENT);
    }

    @Override
    public CommonResponse updateSystemParameter(UpdateSystemParameterDTO updateSystemParameterDTO) {
        log.info("Update system parameter: {}", updateSystemParameterDTO);
        Optional<SystemParameter> systemParameter = systemParameterRepository.findByCode(updateSystemParameterDTO.getCode().trim());
        if(!systemParameter.isPresent()) {
            return CommonResponse.builder().success(false).message("Le paramètre système est introuvable").build();
        }
        systemParameter.get().setValue(updateSystemParameterDTO.getNewValue());
        return CommonResponse.builder().success(true).message("Le paramètre système a été mis à jour").build();
    }
    
}
