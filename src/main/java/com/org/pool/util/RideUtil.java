package com.org.pool.util;

import com.org.pool.domain.dtos.Coordinates;
import com.org.pool.domain.dtos.RouteInfo;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class RideUtil {

    public RouteInfo getRouteInfo(JsonNode routes) {
        return new RouteInfo(routes.path("distance").asDouble(),
                routes.path("duration").asDouble());
    }

}
