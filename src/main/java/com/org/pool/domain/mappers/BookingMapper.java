package com.org.pool.domain.mappers;

import com.org.pool.domain.dtos.CreateBookingResponseDto;
import com.org.pool.domain.entities.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {

    @Mapping(source = "ride.id", target = "rideId")
    CreateBookingResponseDto toDto(Booking booking);

}
