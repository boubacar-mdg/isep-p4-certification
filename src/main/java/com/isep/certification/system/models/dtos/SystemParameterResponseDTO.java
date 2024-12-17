package com.isep.certification.system.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemParameterResponseDTO {
    private String code, value;
}
