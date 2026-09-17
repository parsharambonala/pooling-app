package com.org.pool.domain.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequestDto {
    private String pickupAddress;
    private LocalDateTime pickupTime;
}
