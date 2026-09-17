package com.org.pool.services;

import com.org.pool.domain.dtos.Coordinates;
import com.org.pool.domain.dtos.RouteInfo;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OlaMapsService {
    Coordinates getCode(String pickupAddress);
    JsonNode getRoutes(Coordinates origin, Optional<Coordinates> leg, LocalDateTime departureTime);
}
