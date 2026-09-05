package com.org.pool.domain.dtos;


import com.org.pool.domain.entities.RoleEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeRequestDto {

    @NotNull(message = "employee ID must not be null")
    private Integer employeeId;

    @NotNull(message = "employee mail must not be null")
    private String mailId;

    @NotEmpty(message = "password should not be blank")
    @Size(min = 9, message = "password length should be greater than 8")
    private String password;

    @NotBlank(message = "name should be provided")
    private String name;

    @NotBlank(message = "Address should be provided")
    private String address;

    @NotBlank(message = "Mobile Number shouldn't be blank")
    private String mobileNumber;

    private String manager;

    private String division;

    @NotNull(message = "Role should be selected")
    private RoleEnum role;

    @Valid
    private List<CreateVehicleRequestDto> vehicle;

}
