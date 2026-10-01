package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.factory;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductionFactoryRegisteredMessage(
        @JsonProperty("simulationDay") int simulationDay,
        @JsonProperty("occurredAt") String occurredAt) {}
