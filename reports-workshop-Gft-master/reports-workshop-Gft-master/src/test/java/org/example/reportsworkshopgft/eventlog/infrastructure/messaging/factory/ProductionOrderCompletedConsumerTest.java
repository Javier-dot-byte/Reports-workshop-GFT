package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.factory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.reportsworkshopgft.eventlog.application.impl.EventLogServiceImpl;
import org.example.reportsworkshopgft.eventlog.domain.EventType;
import org.example.reportsworkshopgft.eventlog.domain.SourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductionOrderCompletedConsumerTest {

    private EventLogServiceImpl eventLogService;
    private ProductionEventConsumer consumer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        eventLogService = mock(EventLogServiceImpl.class);
        consumer = new ProductionEventConsumer(eventLogService, objectMapper);
    }

    @Test
    void should_save_event_log_when_production_order_completed_message_is_received() {
        ProductionOrderCompletedMessage message =
                new ProductionOrderCompletedMessage(
                        "order-1", "factory-1", 8, "2026-05-06T14:00:00");

        consumer.onProductionOrderCompleted(message);

        verify(eventLogService)
                .save(
                        eq(EventType.PRODUCTION_ORDER_COMPLETED),
                        eq(SourceService.FACTORY),
                        any(String.class),
                        eq(8),
                        eq("2026-05-06T14:00:00"));
    }
}
