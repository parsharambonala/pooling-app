package com.org.pool.services;


import com.org.pool.domain.CreateEmployeeRequest;
import com.org.pool.domain.entities.Employee;

import java.util.Optional;
import java.util.UUID;


public interface EmployeeService {

    Employee createEmployee(CreateEmployeeRequest createEmployeeRequest);

    Optional<Employee> getEmployeeById(Integer employeeId);

}
