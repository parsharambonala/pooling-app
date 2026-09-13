package com.org.pool.services;

import com.org.pool.domain.CreateRideRequest;
import com.org.pool.domain.entities.Ride;
import org.springframework.security.core.Authentication;

public interface RideService {
    Ride createRide(CreateRideRequest createRideRequest, String mailId);
}
