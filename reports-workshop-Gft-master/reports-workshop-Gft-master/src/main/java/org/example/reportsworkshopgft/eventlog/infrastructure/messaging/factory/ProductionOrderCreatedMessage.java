package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.factory;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductionOrderCreatedMessage(
        @JsonProperty("orderId") String orderId,
        @JsonProperty("factoryId") String factoryId,
        @JsonProperty("productId") String productId,
        @JsonProperty("quantity") int quantity,
        @JsonProperty("simulationDay") int simulationDay,
        @JsonProperty("occurredAt") String occurredAt) {}
