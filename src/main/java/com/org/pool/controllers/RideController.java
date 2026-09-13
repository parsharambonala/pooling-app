package com.org.pool.controllers;


import com.org.pool.domain.CreateRideRequest;
import com.org.pool.domain.dtos.CreateRideRequestDto;
import com.org.pool.domain.dtos.CreateRideResponseDto;
import com.org.pool.domain.entities.Ride;
import com.org.pool.domain.mappers.RideMapper;
import com.org.pool.services.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "rides")
@RequiredArgsConstructor
public class RideController {

    private final RideMapper rideMapper;
    private final RideService rideService;

    @PostMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<CreateRideResponseDto> createRide(
            @Valid @RequestBody CreateRideRequestDto createRideRequestDto,
                    Authentication authentication
            ) {

        String mailId = authentication.getName();

        CreateRideRequest createRideRequest = rideMapper.fromDto(createRideRequestDto);
        Ride rideCreated = rideService.createRide(createRideRequest, mailId);
        CreateRideResponseDto newRide = rideMapper.toDto(rideCreated);
        return ResponseEntity.ok(newRide);

    }

}
