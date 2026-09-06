package com.org.pool.services.impl;

import com.org.pool.config.SecurityConfig;
import com.org.pool.domain.CreateEmployeeRequest;
import com.org.pool.domain.entities.Employee;
import com.org.pool.domain.entities.RoleEnum;
import com.org.pool.domain.entities.User;
import com.org.pool.domain.entities.Vehicle;
import com.org.pool.repositories.EmployeeRepository;
import com.org.pool.services.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Employee createEmployee(CreateEmployeeRequest employee) {

        Employee.EmployeeBuilder employeeBuilder = Employee.builder();

        String encryptedPassword = passwordEncoder.encode(employee.getPassword());

        User userToCreate = User.builder()
                .roles(employee.getRoles())
                .password(encryptedPassword)
                .build();

        Employee employeeToCreate = employeeBuilder.employeeId(employee.getEmployeeId())
                .mailId(employee.getMailId())
                .name(employee.getName())
                .address(employee.getAddress())
                .mobileNumber(employee.getMobileNumber())
                .user(userToCreate)
                .build();

        if(employee.getManager() != null) {
            employeeToCreate.setManager(employee.getManager());
        }

        if(employee.getDivision() != null) {
            employeeToCreate.setDivision(employee.getDivision());
        }

        if(employee.getRoles().contains(RoleEnum.DRIVER)) {

            List<Vehicle> vehiclesToCreate = employee.getVehicle().stream().map( vehicle -> {
                return Vehicle.builder()
                        .vehicleNumber(vehicle.getVehicleNumber())
                        .vehicleModel(vehicle.getVehicleModel())
                        .vehicleType(vehicle.getVehicleType())
                        .seatCount(vehicle.getSeatCount())
                        .employee(employeeToCreate)
                        .build();
            } ).toList();


            employeeToCreate.setVehicles(vehiclesToCreate);
        }

        return employeeRepository.save(employeeToCreate);

    }

    @Override
    public Optional<Employee> getEmployeeById(Integer employeeId) {

        return employeeRepository.findById(employeeId);

    }
}
