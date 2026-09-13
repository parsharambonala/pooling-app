package com.org.pool.services.impl;

import com.org.pool.domain.CreateRideRequest;
import com.org.pool.domain.entities.Employee;
import com.org.pool.domain.entities.Ride;
import com.org.pool.domain.entities.RideStatusEnum;
import com.org.pool.domain.entities.Vehicle;
import com.org.pool.repositories.EmployeeRepository;
import com.org.pool.repositories.RideRepository;
import com.org.pool.repositories.VehicleRepository;
import com.org.pool.services.RideService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final EmployeeRepository employeeRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public Ride createRide(CreateRideRequest createRideRequest, String mailId) {

        Employee employee = employeeRepository.findByMailId(mailId);
        Vehicle vehicle = vehicleRepository.findById(createRideRequest.getVehicleId()).orElseThrow(() ->  new EntityNotFoundException("Vehicle Not Found"));

        Ride rideToCreate = Ride.builder()
                .driver(employee)
                .vehicle(vehicle)
                .status(RideStatusEnum.SCHEDULED)
                .departureTime(createRideRequest.getDepartureTime())
                .startAddress(createRideRequest.getStartAddress())
                .build();

        return rideRepository.save(rideToCreate);

    }
}
