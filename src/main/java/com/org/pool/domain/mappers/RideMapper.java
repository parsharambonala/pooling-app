package com.org.pool.domain.mappers;

import com.org.pool.domain.CreateRideRequest;
import com.org.pool.domain.SearchRideRequest;
import com.org.pool.domain.dtos.CreateRideRequestDto;
import com.org.pool.domain.dtos.CreateRideResponseDto;
import com.org.pool.domain.dtos.SearchRideRequestDto;
import com.org.pool.domain.entities.Ride;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RideMapper {
    CreateRideRequest fromDto(CreateRideRequestDto createRideRequestDto);

    @Mapping(source = "driver.name", target = "driverName")
    @Mapping(source = "vehicle.vehicleModel", target = "vehicleName")
    CreateRideResponseDto toDto(Ride ride);

    SearchRideRequest fromDto(SearchRideRequestDto dto);

}
