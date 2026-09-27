package com.org.pool.domain.dtos;

import com.org.pool.domain.entities.BookingStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBookingRequestsDto {
    private UUID id;
    private String name;
    private String pickupAddress;
    private Integer oneTimeCode;
    private Double distanceFromDestination;
    private Double fare;
    private BookingStatusEnum status;
}
