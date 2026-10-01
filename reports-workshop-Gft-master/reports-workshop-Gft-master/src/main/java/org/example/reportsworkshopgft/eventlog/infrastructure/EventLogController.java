package org.example.reportsworkshopgft.eventlog.infrastructure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.reportsworkshopgft.eventlog.application.EventLogService;
import org.example.reportsworkshopgft.eventlog.application.projections.OrderHistoryProjection;
import org.example.reportsworkshopgft.eventlog.application.projections.SystemStatsProjection;
import org.example.reportsworkshopgft.eventlog.domain.EventLog;
import org.example.reportsworkshopgft.eventlog.domain.EventLogId;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(
        name = "Dashboard and Reports",
        description = "Endpoints for querying factory and transport status")
public class EventLogController {

    private final EventLogService eventLogService;

    @GetMapping({"/", ""})
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get all event logs (paginated)",
            description =
                    "Returns paginated event sourcing log of all system activities. Supports sorting and custom page sizes.")
    public Page<EventLog> getAllEventLogs(
            @Parameter(description = "Page number (0-indexed)", example = "0")
                    @RequestParam(defaultValue = "0")
                    int page,
            @Parameter(description = "Page size (number of records per page)", example = "20")
                    @RequestParam(defaultValue = "20")
                    int size) {
        log.info("Fetching events - page: {}, size: {}", page, size);
        Page<EventLog> eventPage =
                eventLogService.findAllEventsLogs(
                        org.springframework.data.domain.PageRequest.of(page, size));
        log.info(
                "Events found: {} total, {} on this page",
                eventPage.getTotalElements(),
                eventPage.getNumberOfElements());
        return eventPage;
    }

    @GetMapping("/{reports_id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Find event by ID",
            description = "Returns the details of a specific system event.")
    public EventLog getEventLogById(@PathVariable("reports_id") EventLogId id) {
        return eventLogService.findEventLogById(id);
    }

    @GetMapping("/stats")
    @Operation(
            summary = "Get global statistics",
            description =
                    "Calculates real-time totals of processed, completed, and blocked orders, as well as trucks in transit.")
    public ResponseEntity<SystemStatsProjection> getSystemStats() {
        SystemStatsProjection stats = eventLogService.getSystemStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/orders/history")
    @Operation(
            summary = "Get order history (paginated)",
            description =
                    "Returns paginated order status history with most recent status of each order. Supports pagination for large result sets.")
    public ResponseEntity<Page<OrderHistoryProjection>> getOrderHistory(
            @Parameter(description = "Page number (0-indexed)", example = "0")
                    @RequestParam(defaultValue = "0")
                    int page,
            @Parameter(description = "Page size (number of records per page)", example = "50")
                    @RequestParam(defaultValue = "50")
                    int size) {
        log.info("Fetching order history - page: {}, size: {}", page, size);
        Page<OrderHistoryProjection> history = eventLogService.getOrderHistory(page, size);
        log.info(
                "Order history retrieved: {} total, {} on this page",
                history.getTotalElements(),
                history.getNumberOfElements());
        return ResponseEntity.ok(history);
    }
}
