package com.org.pool.notification.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideCancellationNotificationEvent extends RideNotificationEvent {

    private List<Integer> affectedPassengerIds;
}
