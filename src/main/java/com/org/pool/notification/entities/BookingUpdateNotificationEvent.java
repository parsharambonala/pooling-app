package com.org.pool.notification.entities;

import com.org.pool.domain.entities.BookingStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingUpdateNotificationEvent
        extends RideNotificationEvent {

    private UUID bookingId;
    private Integer passengerId;
    private BookingStatusEnum status;

}
