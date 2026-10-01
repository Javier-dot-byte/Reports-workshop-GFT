package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.truck;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record TruckRegisteredEvent(
        @JsonProperty("truckId") UUID truckId,
        @JsonProperty("name") String name,
        @JsonProperty("position") Position position,
        @JsonProperty("capacity") int capacity,
        @JsonProperty("timestamp") int timestamp) {
    public record Position(@JsonProperty("x") int x, @JsonProperty("y") int y) {}
}
