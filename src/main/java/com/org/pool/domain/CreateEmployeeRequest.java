package com.org.pool.domain;


import com.org.pool.domain.entities.RoleEnum;
import com.org.pool.domain.entities.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeRequest {

    private Integer employeeId;
    private String mailId;
    private String password;
    private String name;
    private String address;
    private String mobileNumber;
    private String manager;
    private String division;
    private Set<RoleEnum> roles;
    private List<CreateVehicleRequest> vehicle;

}
