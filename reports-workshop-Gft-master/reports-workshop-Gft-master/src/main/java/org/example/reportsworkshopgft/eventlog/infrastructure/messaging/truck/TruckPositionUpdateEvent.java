package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.truck;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TruckPositionUpdateEvent(
        @JsonProperty("truckId") String truckId,
        @JsonProperty("position") Position position,
        @JsonProperty("simulationDay") int simulationDay,
        @JsonProperty("timestamp") String timestamp) {
    public record Position(@JsonProperty("x") int x, @JsonProperty("y") int y) {}
}
