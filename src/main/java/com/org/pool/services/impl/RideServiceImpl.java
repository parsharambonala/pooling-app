package com.org.pool.services.impl;

import com.org.pool.domain.CreateRideRequest;
import com.org.pool.domain.SearchRideRequest;
import com.org.pool.domain.dtos.Coordinates;
import com.org.pool.domain.dtos.RouteInfo;
import com.org.pool.domain.entities.Employee;
import com.org.pool.domain.entities.Ride;
import com.org.pool.domain.entities.RideStatusEnum;
import com.org.pool.domain.entities.Vehicle;
import com.org.pool.repositories.EmployeeRepository;
import com.org.pool.repositories.RideRepository;
import com.org.pool.repositories.VehicleRepository;
import com.org.pool.services.MapboxService;
import com.org.pool.services.RideService;
import com.org.pool.util.RideUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final EmployeeRepository employeeRepository;
    private final VehicleRepository vehicleRepository;
    private final MapboxService mapboxClient;
    private final RideUtil rideUtil;

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

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    @Override
    public List<Ride> searchRides(SearchRideRequest request) {

        String pickupAddress = request.getPickupAddress();
        LocalDateTime passengerDepartureTime = request.getDepartureTime();
        Coordinates passengerCoordinates = mapboxClient.getCode(pickupAddress);
        List<Ride> relevantRides = getAllRides().stream()
               .filter(ride -> ride.getDepartureTime().toLocalDate().equals(passengerDepartureTime.toLocalDate())
                       && ride.getStatus() == RideStatusEnum.SCHEDULED)
               .toList();

        List<Ride> ridesToReturn = new ArrayList<>();

        for(Ride ride : relevantRides) {

            Coordinates driverCoordinates = mapboxClient.getCode(ride.getStartAddress());
            RouteInfo toOffice = rideUtil.getRouteInfo(mapboxClient.getRoutes(driverCoordinates, null, ride.getDepartureTime()));
            JsonNode withPassengerFullInfo = mapboxClient.getRoutes(driverCoordinates, passengerCoordinates, ride.getDepartureTime());
            RouteInfo withPassenger = rideUtil.getRouteInfo(withPassengerFullInfo);

            Double distanceDiff = withPassenger.distanceMeters() - toOffice.distanceMeters();
            Double durationDiff = withPassenger.durationSeconds() - toOffice.durationSeconds();

            if( distanceDiff.compareTo(5000d) < 0 && durationDiff.compareTo(6000d) < 0){
                Double passengerLegDuration = withPassengerFullInfo.path("legs").get(0).path("duration").asDouble();

                LocalDateTime arrivalTime = ride.getDepartureTime().plusSeconds(passengerLegDuration.longValue());

                if(!arrivalTime.isBefore(passengerDepartureTime.minusMinutes(10)) &&
                !arrivalTime.isAfter(passengerDepartureTime.plusMinutes(10))){
                    ridesToReturn.add(ride);
                }
            }

        }

       return ridesToReturn;
    }
}
