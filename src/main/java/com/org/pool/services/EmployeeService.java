package com.org.pool.services;


import com.org.pool.domain.CreateEmployeeRequest;
import com.org.pool.domain.entities.Employee;
import org.springframework.stereotype.Service;


public interface EmployeeService {

    Employee createEmployee(CreateEmployeeRequest createEmployeeRequest);

}
