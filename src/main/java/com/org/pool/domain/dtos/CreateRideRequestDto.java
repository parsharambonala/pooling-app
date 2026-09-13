package com.org.pool.domain.dtos;

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

    @NotNull(message = "Departure time must be provided")
    private LocalDateTime departureTime;

    @NotBlank(message = "Start Address must be provided")
    private String startAddress;


}
