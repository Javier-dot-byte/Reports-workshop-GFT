package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.truck;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TruckDeletedEvent(
        @JsonProperty("deliveryId") String deliveryId,
        @JsonProperty("truckId") String truckId,
        @JsonProperty("simulationDay") int simulationDay,
        @JsonProperty("timestamp") String timestamp) {}