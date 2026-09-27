package com.org.pool.services;

import com.org.pool.domain.CreateRideRequest;
import com.org.pool.domain.SearchRideRequest;
import com.org.pool.domain.dtos.RideInfo;
import com.org.pool.domain.entities.Booking;
import com.org.pool.domain.entities.BookingStatusEnum;
import com.org.pool.domain.entities.Ride;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.UUID;

public interface RideService {
    Ride createRide(CreateRideRequest createRideRequest, String mailId);
    List<RideInfo> searchRides(SearchRideRequest searchRideRequest);
    Ride getRide(UUID rideId);
    void deleteRide(UUID rideId, String mailId) throws BadRequestException;
    List<Booking> getBookingRequests(UUID rideId, String mailId) throws IllegalAccessException;
    void changeBookingStatus(UUID rideId, UUID bookingId, String mailId, BookingStatusEnum status) throws IllegalAccessException, BadRequestException;
}
