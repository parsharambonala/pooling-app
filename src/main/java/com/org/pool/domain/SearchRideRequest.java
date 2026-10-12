package com.org.pool.domain;

import com.org.pool.domain.entities.RideDirectionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRideRequest {

    private RideDirectionEnum rideDirection;
    private String location;
    private LocalDateTime pickupTime;

}
