package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.truck;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TruckStatusChangedEvent(
        @JsonProperty("truckId") String truckId,
        @JsonProperty("status") String status,
        @JsonProperty("simulationDay") int simulationDay,
        @JsonProperty("timestamp") String timestamp) {}
