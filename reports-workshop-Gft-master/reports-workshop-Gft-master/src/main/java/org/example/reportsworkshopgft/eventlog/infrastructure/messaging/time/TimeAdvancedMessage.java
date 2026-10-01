package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.time;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TimeAdvancedMessage(
        @JsonProperty("previousDay") int simulationDay,
        @JsonProperty("occurredAt") String occurredAt) {}
