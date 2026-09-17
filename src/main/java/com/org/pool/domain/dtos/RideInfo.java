package com.org.pool.domain.dtos;

import com.org.pool.domain.entities.Ride;

import java.time.LocalDateTime;

public record RideInfo(
        Ride ride,
        LocalDateTime arrivalTime
){

}
