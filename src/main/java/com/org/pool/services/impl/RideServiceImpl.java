package com.org.pool.services.impl;

import com.org.pool.domain.CreateRideRequest;
import com.org.pool.domain.SearchRideRequest;
import com.org.pool.domain.dtos.Coordinates;
import com.org.pool.domain.dtos.RideInfo;
import com.org.pool.domain.dtos.RouteInfo;
import com.org.pool.domain.entities.*;
import com.org.pool.notification.entities.BookingUpdateNotificationEvent;
import com.org.pool.notification.entities.RideCancellationNotificationEvent;
import com.org.pool.notification.service.NotificationProducer;
import com.org.pool.repositories.BookingRepository;
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
    private final BookingRepository bookingRepository;
    private final OlaMapsService olaMapsClient;
    private final RideUtil rideUtil;
    private final NotificationProducer notificationProducer;


    @Override
    @Transactional
    public Ride createRide(CreateRideRequest createRideRequest, String mailId) {

        Employee employee = employeeRepository.findByMailId(mailId);
        Vehicle vehicle = vehicleRepository.findById(createRideRequest.getVehicleId()).orElseThrow(() ->  new EntityNotFoundException("Vehicle Not Found"));

        Ride.RideBuilder rideBuilder = Ride.builder();

        rideBuilder
                .driver(employee)
                .vehicle(vehicle)
                .status(RideStatusEnum.SCHEDULED)
                .rideDirection(createRideRequest.getRideDirection())
                .departureTime(createRideRequest.getDepartureTime());

        if(createRideRequest.getRideDirection() == RideDirectionEnum.TO_OFFICE) {
            rideBuilder.startAddress(createRideRequest.getLocation())
                    .destinationAddress(null);
        }else{
            rideBuilder.startAddress(null)
                    .destinationAddress(createRideRequest.getLocation());
        }

        Ride rideToCreate = rideBuilder.build();
        return rideRepository.save(rideToCreate);

    }

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    @Override
    public List<RideInfo> searchRides(SearchRideRequest request) {

        String location = request.getLocation();
        LocalDateTime passengerPickupTime = request.getPickupTime();
        Coordinates passengerCoordinates = olaMapsClient.getCode(location);
        List<Ride> relevantRides = getAllRides().stream()
               .filter(ride -> ride.getDepartureTime().toLocalDate().equals(passengerPickupTime.toLocalDate())
                       && ride.getStatus() == RideStatusEnum.SCHEDULED && ride.getBookings().size() < ride.getVehicle().getSeatCount())
               .toList();

        List<RideInfo> ridesToReturn = new ArrayList<>();

        for(Ride ride : relevantRides) {

            Coordinates driverCoordinates = olaMapsClient.getCode(ride.getRideDirection() == RideDirectionEnum.TO_OFFICE
            ? ride.getStartAddress() : ride.getDestinationAddress());
            RouteInfo withoutPassenger = rideUtil.getRouteInfo(olaMapsClient.getRoutes(driverCoordinates, Optional.empty(), ride.getDepartureTime(), ride.getRideDirection()));
            JsonNode withPassengerFullInfo = olaMapsClient.getRoutes(driverCoordinates, Optional.ofNullable(passengerCoordinates), ride.getDepartureTime(), ride.getRideDirection());
            RouteInfo withPassenger = rideUtil.getRouteInfo(withPassengerFullInfo);

            Double distanceDiff = withPassenger.distanceMeters() - withoutPassenger.distanceMeters();
            Double durationDiff = withPassenger.durationSeconds() - withoutPassenger.durationSeconds();

            if( distanceDiff.compareTo(3000d) < 0 && durationDiff.compareTo(600d) < 0){

                Double passengerLegDuration = withPassengerFullInfo.path("legs").get(0).path("duration").asDouble();
                Double officeLegDuration = withPassengerFullInfo.path("legs").get(1).path("duration").asDouble();

                LocalDateTime arrivalTime = ride.getDepartureTime().plusSeconds(passengerLegDuration.longValue());
                LocalDateTime reachTime = arrivalTime.plusSeconds(officeLegDuration.longValue());

                if(request.getRideDirection() == RideDirectionEnum.FROM_OFFICE) {

                    if(!passengerPickupTime.isAfter(ride.getDepartureTime().plusMinutes(10)) &&
                            !passengerPickupTime.isBefore(ride.getDepartureTime().minusMinutes(10))){

                        ridesToReturn.add(new RideInfo(ride, ride.getDepartureTime(), arrivalTime));

                    }

                }else {

                    if(!arrivalTime.isBefore(passengerPickupTime.minusMinutes(10)) &&
                            !arrivalTime.isAfter(passengerPickupTime.plusMinutes(10))){
                        ridesToReturn.add(new RideInfo(ride, arrivalTime, reachTime));
                    }
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

        if(!ride.getDriver().getMailId().equals(mailId)){
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

        RideCancellationNotificationEvent rideNotificationEvent = new RideCancellationNotificationEvent();
        rideNotificationEvent.setRideId(ride.getId());
        rideNotificationEvent.setDriverId(ride.getDriver().getEmployeeId());
        rideNotificationEvent.setAffectedPassengerIds(affectedPassengers);
        notificationProducer.sendNotification("cancel-ride", rideNotificationEvent);

        ride.setStatus(RideStatusEnum.CANCELLED);

    }

    @Override
    public List<Booking> getBookingRequests(UUID rideId, String mailId) throws IllegalAccessException {
        Ride ride = getRide(rideId);

        if(!ride.getDriver().getMailId().equals(mailId)) {
            throw new IllegalAccessException("Not authorized to access the bookings");
        }

        return ride.getBookings();

    }

    @Override
    public void changeBookingStatus(UUID rideId, UUID bookingId, String mailId, BookingStatusEnum status) throws IllegalAccessException, BadRequestException {

        Ride ride = getRide(rideId);

        if(!ride.getDriver().getMailId().equals(mailId)) {
            throw new IllegalAccessException("Not authorized to access the bookings");
        }

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()->
                new EntityNotFoundException("Booking not found"));

        if(!booking.getRide().equals(ride)) {
            throw new BadRequestException("Booking doesn't belong to this ride");
        }

        if(!isValidTransition(booking.getStatus(), status)) {
            throw new BadRequestException("Invalid status Transition");
        }

        booking.setStatus(status);
        bookingRepository.save(booking);

        BookingUpdateNotificationEvent updateNotificationEvent = new BookingUpdateNotificationEvent();

        updateNotificationEvent.setRideId(rideId);
        updateNotificationEvent.setDriverId(ride.getDriver().getEmployeeId());
        updateNotificationEvent.setDepartureTime(ride.getDepartureTime());
        updateNotificationEvent.setBookingId(bookingId);
        updateNotificationEvent.setPassengerId(booking.getPassenger().getEmployeeId());
        updateNotificationEvent.setStatus(status);

        notificationProducer.sendNotification("update-booking", updateNotificationEvent);


    }


    public boolean isValidTransition(BookingStatusEnum currentStatus, BookingStatusEnum desiredStatus) {

        return (currentStatus == BookingStatusEnum.PENDING && desiredStatus == BookingStatusEnum.ACCEPTED) ||
                currentStatus == BookingStatusEnum.PENDING && desiredStatus == BookingStatusEnum.REJECTED;

    }

}
