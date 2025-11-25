package com.hclSoftware.learningCachingSpringBoot.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeDto implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String departmentName;
}
