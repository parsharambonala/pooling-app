package com.org.pool.services.impl;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.org.pool.domain.dtos.Coordinates;
import com.org.pool.domain.dtos.RouteInfo;
import com.org.pool.domain.entities.Booking;
import com.org.pool.domain.entities.BookingStatusEnum;
import com.org.pool.domain.entities.Employee;
import com.org.pool.domain.entities.Ride;
import com.org.pool.repositories.BookingRepository;
import com.org.pool.services.BookingService;
import com.org.pool.services.OlaMapsService;
import com.org.pool.util.RideUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final OlaMapsService olaMapsClient;
    private final RideUtil rideUtil;

    @Override
    public Booking createBooking(Ride ride, Employee passenger, String pickupAddress, LocalDateTime pickupTime) {

        Coordinates pickupAddressCoordinates = olaMapsClient.getCode(pickupAddress);
        RouteInfo routeInfo = rideUtil.getRouteInfo(olaMapsClient.getRoutes(pickupAddressCoordinates, java.util.Optional.empty(), pickupTime));

        Double fareCalculated = rideUtil.calculateFare(routeInfo, ride.getVehicle().getVehicleType());

        Booking bookingToCreate = Booking.builder()
                .ride(ride)
                .oneTimeCode(new Random().nextInt(1000,9999))
                .fare(fareCalculated)
                .distanceFromDestination(routeInfo.distanceMeters())
                .pickupAddress(pickupAddress)
                .passenger(passenger)
                .status(BookingStatusEnum.COMPLETED)
                .build();

        return bookingRepository.save(bookingToCreate);

    }

    public void deleteBooking(UUID bookingId) {
        bookingRepository.deleteById(bookingId);
    }

}
