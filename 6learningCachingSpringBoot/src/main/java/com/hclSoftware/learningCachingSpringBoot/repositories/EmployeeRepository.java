package com.hclSoftware.learningCachingSpringBoot.repositories;

import com.hclSoftware.learningCachingSpringBoot.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
