package org.example.reportsworkshopgft.eventlog.application.projections;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Consolidated view of the most recent order status")
public record OrderHistoryProjection(
        @Schema(description = "Unique identifier for the order", example = "ORD-9982")
                String orderId,
        @Schema(description = "Identifier of the assigned factory", example = "FAC-A")
                String factoryId,
        @Schema(description = "Current status of the order", example = "COMPLETED") String status,
        @Schema(description = "Simulation day of the last update", example = "5")
                int simulationDay) {}
