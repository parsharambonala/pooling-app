package com.org.pool.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRideResponseDto {

    private UUID id;
    private String driverName;
    private String vehicleName;
    private String status;
    private LocalDateTime departureTime;
    private String startAddress;

}
