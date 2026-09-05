package com.org.pool.domain.dtos;


import com.org.pool.domain.entities.VehicleTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVehicleResponseDto {

    private UUID id;
    private VehicleTypeEnum vehicleType;
    private String vehicleNumber;
    private String vehicleModel;
    private Integer seatCount;

}
