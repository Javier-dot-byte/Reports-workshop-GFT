package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.factory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProductionOrderBlockedMessageTest {

    @Test
    void shouldCreateProductionOrderBlockedMessage() {
        ProductionOrderBlockedMessage message =
                new ProductionOrderBlockedMessage(5, "2026-05-07T10:00:00");

        assertEquals(5, message.simulationDay());
        assertEquals("2026-05-07T10:00:00", message.occurredAt());
    }
}
