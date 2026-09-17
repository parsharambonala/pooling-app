package com.org.pool.util;

import com.org.pool.domain.dtos.Coordinates;
import com.org.pool.domain.dtos.RouteInfo;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;

@Component
public class RideUtil {

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

}
