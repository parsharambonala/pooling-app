package com.org.pool.domain.dtos;

import com.org.pool.domain.entities.BookingStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingResponseDto {

    private UUID id;
    private UUID rideId;
    private Integer oneTimeCode;
    private Double fare;
    private String pickupAddress;
    private Double distanceFromDestination;
    private BookingStatusEnum status;

}
