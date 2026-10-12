package com.org.pool.domain.dtos;

import com.org.pool.domain.entities.RideDirectionEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRideRequestDto {

    @NotNull(message = "ride direction must be provided")
    private RideDirectionEnum rideDirection;

    @NotBlank(message = "Location must be provided")
    private String location;

    @NotNull(message = "Provide the Departure time")
    private LocalDateTime pickupTime;

}
