package com.org.pool.util;

import com.org.pool.domain.dtos.Coordinates;
import com.org.pool.domain.dtos.RouteInfo;
import com.org.pool.domain.entities.VehicleTypeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;

@Component
public class RideUtil {

    @Value("${fare.base-price}")
    private Double baseFare;

    @Value("${fare.bike.cost-per-km}")
    private Double bikePerKm;

    @Value("${fare.car.cost-per-km}")
    private Double carPerKm;

    public RouteInfo getRouteInfo(JsonNode routes) {
        ArrayNode LegRoutes = (ArrayNode) routes.path("legs");

        Double distance = LegRoutes.get(0).path("distance").asDouble();
        Double duration = LegRoutes.get(0).path("duration").asDouble();

        if(LegRoutes.size() == 2) {
            return new RouteInfo( distance + LegRoutes.get(1).path("distance").asDouble() ,
                    duration + LegRoutes.get(1).path("duration").asDouble());
        }

        return new RouteInfo(distance, duration);
    }

    public Double calculateFare(RouteInfo routeInfo, VehicleTypeEnum vehicleType) {

        Double fare = baseFare;
        Double toKm = routeInfo.distanceMeters()/1000;

        if(vehicleType.equals(VehicleTypeEnum.TWO_WHEELER)) {
            return fare + (bikePerKm * toKm);
        }else{
            return fare + (carPerKm * toKm);
        }

    }
}
