package com.org.pool.services;

import com.org.pool.domain.dtos.Coordinates;
import com.org.pool.domain.dtos.RouteInfo;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public interface MapboxService {
    Coordinates getCode(String pickupAddress);
    JsonNode getRoutes(Coordinates origin, Coordinates leg, LocalDateTime departureTime);
}
