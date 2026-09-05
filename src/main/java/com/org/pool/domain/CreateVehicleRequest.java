package com.org.pool.domain;

import com.org.pool.domain.entities.VehicleTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVehicleRequest {

    private VehicleTypeEnum vehicleType;
    private String vehicleNumber;
    private String vehicleModel;
    private Integer seatCount;

}
