package com.isep.certification.system.services;

import java.util.List;

import com.isep.certification.commons.CommonResponse;
import com.isep.certification.system.models.dtos.UpdateSystemParameterDTO;
import com.isep.certification.system.models.entities.SystemParameter;

public interface SystemParameterService {
    public String getParameterValueByCode(String code);
    public String getParameterValueByCode(String code, String defaultValue);
    public CommonResponse updateSystemParameter(UpdateSystemParameterDTO updateSystemParameterDTO);
    public List<SystemParameter> getAllSystemParametersForClient();
}
