package com.org.pool.notification.Impl;

import com.org.pool.domain.entities.Employee;
import com.org.pool.domain.entities.Notification;
import com.org.pool.domain.entities.Ride;
import com.org.pool.notification.entities.BookingUpdateNotificationEvent;
import com.org.pool.notification.entities.RideCancellationNotificationEvent;
import com.org.pool.notification.entities.RideNotificationEvent;
import com.org.pool.repositories.EmployeeRepository;
import com.org.pool.repositories.NotificationRepository;
import com.org.pool.repositories.RideRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReceiveNotifications {

    private final RideRepository rideRepository;
    private final EmployeeRepository employeeRepository;
    private final NotificationRepository notificationRepository;

    public void receiveRideCancellationNotifications(RideNotificationEvent event, RideCancellationNotificationEvent cancellationEvent) {
        Ride ride = rideRepository.findById(event.getRideId()).orElseThrow(() ->
                new EntityNotFoundException("Ride not found"));

        String message = "The ride with id: " + ride.getId() + ", driver starting from : " + ride.getStartAddress() + "has been cancelled";

        List<Integer> employeeIds = cancellationEvent.getAffectedPassengerIds();

        for (Integer id : employeeIds) {

            Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));


            Notification notification = Notification.builder()
                    .passenger(employee)
                    .message(message)
                    .build();

            notificationRepository.save(notification);
        }
    }

    public void receiveBookingUpdateNotification(RideNotificationEvent event, BookingUpdateNotificationEvent bookingUpdateEvent) {

        Ride ride = rideRepository.findById(bookingUpdateEvent.getRideId()).orElseThrow(
                () -> new EntityNotFoundException("Ride not found")
        );

        String message = "Your Booking request has a status Update: "+ bookingUpdateEvent.getStatus() + "for ride :" + event.getRideId();

        Employee employee = employeeRepository.findById(bookingUpdateEvent.getPassengerId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));


        Notification notification = Notification.builder()
                .passenger(employee)
                .message(message)
                .build();

        notificationRepository.save(notification);

    }

}

