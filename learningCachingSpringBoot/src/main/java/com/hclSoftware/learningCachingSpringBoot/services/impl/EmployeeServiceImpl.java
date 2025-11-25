package com.hclSoftware.learningCachingSpringBoot.services.impl;

import com.hclSoftware.learningCachingSpringBoot.dto.EmployeeDto;
import com.hclSoftware.learningCachingSpringBoot.entities.Department;
import com.hclSoftware.learningCachingSpringBoot.entities.Employee;
import com.hclSoftware.learningCachingSpringBoot.exceptions.ResourceNotFoundException;
import com.hclSoftware.learningCachingSpringBoot.repositories.EmployeeRepository;
import com.hclSoftware.learningCachingSpringBoot.services.EmployeeService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    @CachePut(value = "employees", key = "#result.id")
    public EmployeeDto createEmployee(EmployeeDto dto) {

        Department dep = new Department();
        dep.setName(dto.getDepartmentName());

        Employee emp = new Employee();
        emp.setName(dto.getName());
        emp.setEmail(dto.getEmail());
        emp.setDepartment(dep);

        Employee saved = repository.save(emp);

        return convertToDto(saved);
    }

    @Override
    @Cacheable(cacheNames = "employees", key = "#id")   // Create id: "employees::2" if id = 2
    public EmployeeDto getEmployee(Long id) {
        Employee emp = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return convertToDto(emp);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "employees", key = "#id")
    public void deleteEmployee(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found");
        }
        repository.deleteById(id);
    }

    private EmployeeDto convertToDto(Employee emp) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(emp.getId());
        dto.setName(emp.getName());
        dto.setEmail(emp.getEmail());
        dto.setDepartmentName(emp.getDepartment().getName());
        return dto;
    }
}
