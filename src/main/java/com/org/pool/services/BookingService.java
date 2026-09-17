package com.org.pool.services;


import com.org.pool.domain.entities.Booking;
import com.org.pool.domain.entities.Employee;
import com.org.pool.domain.entities.Ride;

import java.time.LocalDateTime;
import java.util.UUID;

public interface BookingService {
    Booking createBooking(Ride ride, Employee passenger, String pickupAddress, LocalDateTime pickupTime);
    void deleteBooking(UUID bookingId);
}
