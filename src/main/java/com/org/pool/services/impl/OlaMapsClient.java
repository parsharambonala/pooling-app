package com.org.pool.services.impl;

import com.org.pool.domain.dtos.Coordinates;
import com.org.pool.services.OlaMapsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class OlaMapsClient implements OlaMapsService {

    private final RestClient restClient;
    private final String apiKey;

    private final Coordinates officeCoordinates = new Coordinates(17.4355, 78.3851);;

    public OlaMapsClient(@Value("${olamaps.base-url}") String baseUrl, @Value("${olamaps.api-key}") String apiKey) {
        restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.apiKey = apiKey;

    }

    @Override
    public Coordinates getCode(String pickupAddress) {

        JsonNode response = restClient.get()
                        .uri( uriBuilder ->
                                uriBuilder
                                        .path("/places/v1/geocode")
                                        .queryParam("address", pickupAddress)
                                        .queryParam("api_key", apiKey)
                                        .build()
                                )
                        .retrieve()
                        .body(JsonNode.class);

        JsonNode coordinates = response.path("geocodingResults")
                                .get(0)
                                .path("geometry")
                                .path("location");

        return new Coordinates(coordinates.get("lat").asDouble(), coordinates.get("lng").asDouble());

    }

    @Override
    public JsonNode getRoutes(Coordinates origin, Optional<Coordinates> leg ,LocalDateTime departureTime) {

        String originCoordinates = origin.latitude() + "," + origin.longitude();
        String destinationCoordinates = officeCoordinates.latitude() + "," + officeCoordinates.longitude();

        JsonNode response = restClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/routing/v1/directions")
                                .queryParam("origin", originCoordinates)
                                .queryParam("destination", destinationCoordinates)
                                .queryParamIfPresent("waypoints", Optional.of(leg.map(c -> c.latitude() + "," + c.longitude())))
                                .queryParam("steps", false)
                                .queryParam("api_key", apiKey)
                                .build()
                        )
                .retrieve()
                .body(JsonNode.class);

        return response.path("routes").get(0);

    }
}
