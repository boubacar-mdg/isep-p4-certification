package com.isep.certification.config.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isep.certification.config.services.ConfigService;
import com.isep.certification.system.models.dtos.SystemParameterResponseDTO;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/config")
@RequiredArgsConstructor
@Hidden
public class ConfigController {

    private final ConfigService configService;

    @GetMapping("/application")
    public ResponseEntity<List<SystemParameterResponseDTO>> getSystemConfig() {
        return ResponseEntity.ok(configService.getSystemValues());
    }
    @PostMapping("/mock/upload")
    public ResponseEntity<Void> mockUpload() {
        return ResponseEntity.ok().build();
    }

    

}
