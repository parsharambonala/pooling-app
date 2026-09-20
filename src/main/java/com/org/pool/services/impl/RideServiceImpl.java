package com.org.pool.services.impl;

import com.org.pool.domain.CreateRideRequest;
import com.org.pool.domain.SearchRideRequest;
import com.org.pool.domain.dtos.Coordinates;
import com.org.pool.domain.dtos.RideInfo;
import com.org.pool.domain.dtos.RouteInfo;
import com.org.pool.domain.entities.*;
import com.org.pool.notification.entities.RideCancellationNotificationEvent;
import com.org.pool.notification.entities.RideNotificationEvent;
import com.org.pool.notification.service.NotificationConsumer;
import com.org.pool.notification.service.NotificationProducer;
import com.org.pool.repositories.EmployeeRepository;
import com.org.pool.repositories.RideRepository;
import com.org.pool.repositories.VehicleRepository;
import com.org.pool.services.OlaMapsService;
import com.org.pool.services.RideService;
import com.org.pool.util.RideUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final EmployeeRepository employeeRepository;
    private final VehicleRepository vehicleRepository;
    private final OlaMapsService olaMapsClient;
    private final RideUtil rideUtil;


    @Override
    @Transactional
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
    public List<RideInfo> searchRides(SearchRideRequest request) {

        String pickupAddress = request.getPickupAddress();
        LocalDateTime passengerPickupTime = request.getDepartureTime();
        Coordinates passengerCoordinates = olaMapsClient.getCode(pickupAddress);
        List<Ride> relevantRides = getAllRides().stream()
               .filter(ride -> ride.getDepartureTime().toLocalDate().equals(passengerPickupTime.toLocalDate())
                       && ride.getStatus() == RideStatusEnum.SCHEDULED && ride.getBookings().size() < ride.getVehicle().getSeatCount())
               .toList();

        List<RideInfo> ridesToReturn = new ArrayList<>();

        for(Ride ride : relevantRides) {

            Coordinates driverCoordinates = olaMapsClient.getCode(ride.getStartAddress());
            RouteInfo toOffice = rideUtil.getRouteInfo(olaMapsClient.getRoutes(driverCoordinates, Optional.empty(), ride.getDepartureTime()));
            JsonNode withPassengerFullInfo = olaMapsClient.getRoutes(driverCoordinates, Optional.ofNullable(passengerCoordinates), ride.getDepartureTime());
            RouteInfo withPassenger = rideUtil.getRouteInfo(withPassengerFullInfo);

            Double distanceDiff = withPassenger.distanceMeters() - toOffice.distanceMeters();
            Double durationDiff = withPassenger.durationSeconds() - toOffice.durationSeconds();

            if( distanceDiff.compareTo(5000d) < 0 && durationDiff.compareTo(600d) < 0){
                Double passengerLegDuration = withPassengerFullInfo.path("legs").get(0).path("duration").asDouble();

                LocalDateTime arrivalTime = ride.getDepartureTime().plusSeconds(passengerLegDuration.longValue());

                if(!arrivalTime.isBefore(passengerPickupTime.minusMinutes(10)) &&
                !arrivalTime.isAfter(passengerPickupTime.plusMinutes(10))){
                    ridesToReturn.add(new RideInfo(ride, arrivalTime));
                }
            }

        }

       return ridesToReturn;
    }

    @Override
    public Ride getRide(UUID rideId) {
       return rideRepository.findById(rideId).filter( ridee -> ridee.getStatus() != RideStatusEnum.CANCELLED).orElseThrow(
                () -> new EntityNotFoundException("Ride Not Found")
       );

    }

    @Override
    @Transactional
    public void deleteRide(UUID rideId, String mailId) throws BadRequestException {

        Ride ride =  getRide(rideId);
        Employee driver = employeeRepository.findByMailId(mailId);

        if(!ride.getDriver().getName().equals(driver.getName())){
            throw new BadRequestException("Unauthorized to delete");
        }

        if(ride.getStatus() != RideStatusEnum.SCHEDULED){
            throw new IllegalStateException("Cannot cancel the ride now");
        }

        List<Integer> affectedPassengers = new ArrayList<>();
        List<Booking> bookings = ride.getBookings();

        bookings.forEach(booking ->
                        booking.setStatus(BookingStatusEnum.CANCELLED));

        affectedPassengers = bookings.stream().map(booking ->
                booking.getPassenger().getEmployeeId()).toList();

        NotificationProducer notificationProducer = new NotificationProducer("cancel-ride");

        RideCancellationNotificationEvent rideNotificationEvent = new RideCancellationNotificationEvent();
        rideNotificationEvent.setRideId(ride.getId());
        rideNotificationEvent.setDriverId(ride.getDriver().getEmployeeId());
        rideNotificationEvent.setAffectedPassengerIds(affectedPassengers);
        notificationProducer.sendNotification(rideNotificationEvent);

        ride.setStatus(RideStatusEnum.CANCELLED);

    }
}
