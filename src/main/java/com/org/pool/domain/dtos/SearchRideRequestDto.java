package com.org.pool.domain.dtos;

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

    @NotBlank(message = "Pickup Address must be provided")
    private String pickupAddress;

    @NotNull(message = "Provide the Departure time")
    private LocalDateTime departureTime;

}
