package com.org.pool.domain.dtos;

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
public class CreateRideRequestDto {

    @NotNull(message = "vehicle must be provided")
    private UUID vehicleId;

    @NotNull(message = "ride direction must be provided")
    private RideDirectionEnum rideDirection;

    @NotNull(message = "Departure time must be provided")
    private LocalDateTime departureTime;

    @NotBlank(message = "Location must be provided")
    private String location;


}
