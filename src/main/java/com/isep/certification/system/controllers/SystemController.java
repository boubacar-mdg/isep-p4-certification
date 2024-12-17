package com.isep.certification.system.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isep.certification.commons.CommonResponse;
import com.isep.certification.system.models.dtos.UpdateSystemParameterDTO;
import com.isep.certification.system.services.SystemParameterService;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/system")
@Hidden
public class SystemController {

    private final SystemParameterService systemParameterService;

    @PostMapping("/update")
    public ResponseEntity<CommonResponse> updateSystemParameter(@RequestBody UpdateSystemParameterDTO updateSystemParameterDTO) {
        return ResponseEntity.ok(systemParameterService.updateSystemParameter(updateSystemParameterDTO));
    }

}
