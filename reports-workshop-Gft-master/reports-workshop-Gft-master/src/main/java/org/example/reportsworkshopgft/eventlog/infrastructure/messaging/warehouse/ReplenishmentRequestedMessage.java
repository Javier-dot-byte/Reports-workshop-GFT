package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.warehouse;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReplenishmentRequestedMessage(
        @JsonProperty("productId") String productId,
        @JsonProperty("quantity") int quantity,
        @JsonProperty("type") String type) {
    public String toPayload() {
        return "{\"productId\":\""
                + productId
                + "\",\"quantity\":"
                + quantity
                + ",\"type\":\""
                + type
                + "\"}";
    }
}
