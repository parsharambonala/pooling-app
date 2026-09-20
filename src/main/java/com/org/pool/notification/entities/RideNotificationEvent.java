package com.org.pool.notification.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class RideNotificationEvent {

    private UUID rideId;
    private Integer driverId;
    private LocalDateTime departureTime;

}
