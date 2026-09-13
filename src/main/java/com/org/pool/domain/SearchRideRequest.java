package com.org.pool.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRideRequest {

    private String pickupAddress;
    private LocalDateTime departureTime;

}
