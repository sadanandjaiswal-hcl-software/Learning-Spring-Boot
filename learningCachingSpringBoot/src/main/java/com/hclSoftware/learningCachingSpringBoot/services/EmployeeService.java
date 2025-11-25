package com.hclSoftware.learningCachingSpringBoot.services;

import com.hclSoftware.learningCachingSpringBoot.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto createEmployee(EmployeeDto dto);

    EmployeeDto getEmployee(Long id);

    List<EmployeeDto> getAllEmployees();

    void deleteEmployee(Long id);
}
