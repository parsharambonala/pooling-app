package com.org.pool.domain.dtos;


import com.org.pool.domain.entities.VehicleTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVehicleRequestDto {

    @NotNull(message = "select vehicle type")
    private VehicleTypeEnum vehicleType;

    @NotBlank(message = "Vehicle Number should be provided")
    private String vehicleNumber;

    @NotBlank(message = "Vehicle Model should be provided")
    private String vehicleModel;

    @NotNull(message = "seat count should be provided")
    private Integer seatCount;

}
