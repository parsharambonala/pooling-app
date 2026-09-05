package com.org.pool.controllers;

import com.org.pool.domain.CreateEmployeeRequest;
import com.org.pool.domain.dtos.CreateEmployeeRequestDto;
import com.org.pool.domain.dtos.CreateEmployeeResponseDto;
import com.org.pool.domain.entities.Employee;
import com.org.pool.domain.mappers.EmployeeMapper;
import com.org.pool.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "employees")
public class EmployeeController {

    private final EmployeeMapper employeeMapper;
    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<CreateEmployeeResponseDto> createEmployee(
            @Valid @RequestBody CreateEmployeeRequestDto createEmployeeRequestDto
            ){
        CreateEmployeeRequest createEmployeeRequest = employeeMapper.fromCreateEmployeeRequestDto(createEmployeeRequestDto);
        Employee employee = employeeService.createEmployee(createEmployeeRequest);
        CreateEmployeeResponseDto createEmployeeResponseDto = employeeMapper.toCreateEmployeeResponseDto(employee);
        return new ResponseEntity<>(createEmployeeResponseDto, HttpStatus.CREATED);

    }


}
