package com.isep.certification.system.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateSystemParameterDTO {
    private String code;
    private String newValue;
}
