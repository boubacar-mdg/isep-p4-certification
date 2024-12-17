package com.isep.certification.system.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isep.certification.system.models.ParameterScope;
import com.isep.certification.system.models.entities.SystemParameter;

public interface SystemParameterRepository extends JpaRepository<SystemParameter, Long> {
    Optional<SystemParameter>  findByCode(String code);
    List<SystemParameter> findAllByParameterScope(ParameterScope parameterScope);
}
