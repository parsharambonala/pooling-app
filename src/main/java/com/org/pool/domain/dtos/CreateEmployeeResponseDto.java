package com.org.pool.domain.dtos;


import com.org.pool.domain.entities.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeResponseDto {

    private Integer employeeId;
    private String mailId;
    private String name;
    private String address;
    private String mobileNumber;
    private Set<RoleEnum> roles;
    private List<CreateVehicleResponseDto> vehicle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
