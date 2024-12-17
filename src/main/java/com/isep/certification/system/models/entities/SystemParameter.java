package com.isep.certification.system.models.entities;


import com.isep.certification.system.models.ParameterScope;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "system_parameters")
public class SystemParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_system_parameters")
    @SequenceGenerator(name = "seq_system_parameters", sequenceName = "seq_system_parameters", allocationSize = 20, initialValue = 10)
    private Long id;
    @Column(columnDefinition = "TEXT") 
    private String value;
    private String code;
    private String label;
    @Enumerated(EnumType.STRING)
    private ParameterScope parameterScope;
}
 
