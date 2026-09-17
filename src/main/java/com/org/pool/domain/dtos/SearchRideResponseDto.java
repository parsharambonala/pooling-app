package com.org.pool.domain.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRideResponseDto {

    private UUID rideId;
    private String name;
    private String mobileNumber;
    private String vehicleNumber;
    private String vehicleModel;
    private LocalDateTime departureTime;
    private LocalDateTime pickupTime;

}
