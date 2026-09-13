package com.org.pool.services.impl;

import com.org.pool.domain.dtos.Coordinates;
import com.org.pool.services.MapboxService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;

@Component
public class MapboxClient implements MapboxService {

    private final RestClient restClient;
    private final String accessToken;

    private final Coordinates officeCoordinates = new Coordinates(17.4515, 78.3760);;

    public MapboxClient(@Value("${mapbox.base-url}") String baseUrl, @Value("${mapbox.access-token}") String accessToken) {
        restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.accessToken = accessToken;

    }

    @Override
    public Coordinates getCode(String pickupAddress) {

        JsonNode response = restClient.get()
                        .uri( uriBuilder ->
                                uriBuilder
                                        .path("/search/geocode/v6/forward")
                                        .queryParam("q", pickupAddress)
                                        .queryParam("access_token", accessToken)
                                        .build()
                                )
                        .retrieve()
                        .body(JsonNode.class);

        JsonNode coordinates = response.path("features")
                                .get(0)
                                .path("geometry")
                                .path("coordinates");

        return new Coordinates(coordinates.get(1).asDouble(), coordinates.get(0).asDouble());

    }

    @Override
    public JsonNode getRoutes(Coordinates origin, Coordinates leg ,LocalDateTime departureTime) {

        String coordinates = origin.longitude() + "," + origin.latitude();

        if( leg != null ){
            coordinates = coordinates + ";" + leg.longitude() + "," + leg.latitude();
        }

        coordinates = coordinates + ";" + officeCoordinates.longitude() + "," + officeCoordinates.latitude();

        String finalCoordinates = coordinates;
        JsonNode response = restClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/directions/v5/mapbox/driving-traffic/"+ finalCoordinates)
                                .queryParam("depart_at", departureTime)
                                .queryParam("overview", "full")
                                .queryParam("access_token", accessToken)
                                .build()
                        )
                .retrieve()
                .body(JsonNode.class);

        return response.path("routes").get(0);

    }
}
