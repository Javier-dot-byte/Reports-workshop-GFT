package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.warehouse;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WarehouseStockChangedMessage(
        @JsonProperty("productId") String productId,
        @JsonProperty("quantity") int quantity,
        @JsonProperty("type") String type) {}
