package com.org.pool.domain.dtos;

import com.org.pool.domain.entities.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetEmployeeResponseDto {

    private Integer employeeId;
    private String mailId;
    private String name;
    private String address;
    private String mobileNumber;
    private RoleEnum role;
    private List<CreateVehicleResponseDto> vehicle;

}

