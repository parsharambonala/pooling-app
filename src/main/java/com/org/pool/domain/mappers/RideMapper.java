package com.org.pool.domain.mappers;

import com.org.pool.domain.CreateRideRequest;
import com.org.pool.domain.SearchRideRequest;
import com.org.pool.domain.dtos.*;
import com.org.pool.domain.entities.Booking;
import com.org.pool.domain.entities.Ride;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RideMapper {
    CreateRideRequest fromDto(CreateRideRequestDto createRideRequestDto);

    @Mapping(source = "driver.name", target = "driverName")
    @Mapping(source = "vehicle.vehicleModel", target = "vehicleName")
    @Mapping(target = "location", expression = "java(mapLocation(ride))")
    CreateRideResponseDto toDto(Ride ride);

    default String mapLocation(Ride ride) {
        return ride.getStartAddress() != null ?
                ride.getStartAddress() :
                ride.getDestinationAddress();
    }

    SearchRideRequest fromDto(SearchRideRequestDto dto);

    @Mapping(source = "rideInfo.ride.id", target = "rideId")
    @Mapping(source = "rideInfo.ride.driver.name", target = "name")
    @Mapping(source = "rideInfo.ride.driver.mobileNumber", target = "mobileNumber")
    @Mapping(source = "rideInfo.ride.vehicle.vehicleModel", target = "vehicleModel")
    @Mapping(source = "rideInfo.ride.vehicle.vehicleNumber", target = "vehicleNumber")
    @Mapping(source = "rideInfo.ride.departureTime", target = "departureTime")
    @Mapping(source = "arrivalTime", target = "pickupTime")
    SearchRideResponseDto toSearchDto(RideInfo rideInfo);

    List<SearchRideResponseDto> toDto(List<RideInfo> rideinfos);

    @Mapping(source = "passenger.name", target = "name")
    GetBookingRequestsDto toBookingDto(Booking booking);

    List<GetBookingRequestsDto> toBookingRequestDto(List<Booking> bookings);

}
