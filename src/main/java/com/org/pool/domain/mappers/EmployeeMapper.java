package com.org.pool.domain.mappers;

import com.org.pool.domain.CreateEmployeeRequest;
import com.org.pool.domain.CreateVehicleRequest;
import com.org.pool.domain.dtos.CreateEmployeeRequestDto;
import com.org.pool.domain.dtos.CreateEmployeeResponseDto;
import com.org.pool.domain.dtos.CreateVehicleResponseDto;
import com.org.pool.domain.dtos.GetEmployeeResponseDto;
import com.org.pool.domain.entities.Employee;
import com.org.pool.domain.entities.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    CreateEmployeeRequest fromCreateEmployeeRequestDto(CreateEmployeeRequestDto dto);

    CreateVehicleRequest fromCreateVehicleRequestDto(CreateEmployeeRequestDto dto);

    @Mapping(source = "user.roles", target = "roles")
    @Mapping(source = "vehicles", target = "vehicle")
    CreateEmployeeResponseDto toCreateEmployeeResponseDto(Employee employee);

    CreateVehicleResponseDto toCreateVehicleResponseDto(Vehicle vehicle);

    @Mapping(source = "user.roles", target = "roles")
    @Mapping(source = "vehicles", target = "vehicle")
    GetEmployeeResponseDto toGetEmployeeResponseDto(Employee employee);

}
