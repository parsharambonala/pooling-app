package com.org.pool.controllers;


import com.org.pool.domain.dtos.CreateBookingRequestDto;
import com.org.pool.domain.dtos.CreateBookingResponseDto;
import com.org.pool.domain.entities.Booking;
import com.org.pool.domain.entities.Employee;
import com.org.pool.domain.entities.Ride;
import com.org.pool.domain.entities.RideStatusEnum;
import com.org.pool.domain.mappers.BookingMapper;
import com.org.pool.repositories.BookingRepository;
import com.org.pool.repositories.EmployeeRepository;
import com.org.pool.services.BookingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/rides")
public class BookingController {

    private final RideController rideController;
    private final BookingService bookingService;
    private final EmployeeRepository employeeRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @PostMapping("/{rideId}/book")
    public ResponseEntity<CreateBookingResponseDto> createBooking(
            @PathVariable UUID rideId,
            @RequestBody CreateBookingRequestDto requestDto,
            Authentication authentication
            ) {

        Ride ride = rideController.getRide(rideId).getBody();
        Employee passenger = employeeRepository.findByMailId(authentication.getName());

        if(ride.getStatus() != RideStatusEnum.SCHEDULED) {
            throw new UnsupportedOperationException("Ride is not scheduled");
        }

        if( ride.getBookings().size() == ride.getVehicle().getSeatCount()) {
            throw new UnsupportedOperationException("No seats available");
        }

        Booking booking = bookingService.createBooking(ride, passenger, requestDto.getPickupAddress(), requestDto.getPickupTime());
        return new ResponseEntity<>(bookingMapper.toDto(booking), HttpStatus.CREATED);

    }

    @DeleteMapping(path = "/{bookingId}/book")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable UUID bookingId,
            Authentication authentication
    ) throws BadRequestException {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new EntityNotFoundException("Invalid Booking Id")
        );

        if( !booking.getPassenger().getMailId().equals(authentication.getName())) {
            throw new BadRequestException("Not Authorized");
        }

        bookingService.deleteBooking(bookingId);

        return ResponseEntity.noContent().build();

    }

}
