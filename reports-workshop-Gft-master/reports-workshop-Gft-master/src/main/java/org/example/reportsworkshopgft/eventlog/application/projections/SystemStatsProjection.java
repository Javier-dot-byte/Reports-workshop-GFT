package org.example.reportsworkshopgft.eventlog.application.projections;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Real-time metrics for production and transport status")
public record SystemStatsProjection(
        @Schema(description = "Total number of created orders", example = "150") int totalOrders,
        @Schema(description = "Number of completed orders", example = "85") int completedOrders,
        @Schema(description = "Number of blocked orders", example = "3") int blockedOrders,
        @Schema(description = "Number of trucks currently in transit", example = "12")
                int trucksInTransit) {}
