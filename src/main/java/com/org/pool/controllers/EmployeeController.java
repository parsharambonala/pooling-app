package com.org.pool.controllers;

import com.org.pool.domain.CreateEmployeeRequest;
import com.org.pool.domain.dtos.CreateEmployeeRequestDto;
import com.org.pool.domain.dtos.CreateEmployeeResponseDto;
import com.org.pool.domain.dtos.GetEmployeeResponseDto;
import com.org.pool.domain.entities.Employee;
import com.org.pool.domain.mappers.EmployeeMapper;
import com.org.pool.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "employees")
public class EmployeeController {

    private final EmployeeMapper employeeMapper;
    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<CreateEmployeeResponseDto> createEmployee(
            @Valid @RequestBody CreateEmployeeRequestDto createEmployeeRequestDto
            ) {

        CreateEmployeeRequest createEmployeeRequest = employeeMapper.fromCreateEmployeeRequestDto(createEmployeeRequestDto);
        Employee employee = employeeService.createEmployee(createEmployeeRequest);
        CreateEmployeeResponseDto createEmployeeResponseDto = employeeMapper.toCreateEmployeeResponseDto(employee);
        return new ResponseEntity<>(createEmployeeResponseDto, HttpStatus.CREATED);

    }

    @GetMapping(path = "/{employeeId}")
    public ResponseEntity<GetEmployeeResponseDto> getEmployee(
            @PathVariable Integer employeeId
            ) {
        return employeeService.getEmployeeById(employeeId)
                .map(employeeMapper::toGetEmployeeResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
