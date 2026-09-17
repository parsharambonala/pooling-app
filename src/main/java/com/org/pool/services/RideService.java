package com.org.pool.services;

import com.org.pool.domain.CreateRideRequest;
import com.org.pool.domain.SearchRideRequest;
import com.org.pool.domain.dtos.RideInfo;
import com.org.pool.domain.entities.Ride;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface RideService {
    Ride createRide(CreateRideRequest createRideRequest, String mailId);
    List<RideInfo> searchRides(SearchRideRequest searchRideRequest);
}
