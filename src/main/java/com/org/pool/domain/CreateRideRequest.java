package com.org.pool.domain;

import com.org.pool.domain.entities.RideDirectionEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRideRequest {

    private UUID vehicleId;
    private RideDirectionEnum rideDirection;
    private LocalDateTime departureTime;
    private String location;

}
