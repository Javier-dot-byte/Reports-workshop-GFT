package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.truck;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DeliveryCreatedEventTest {

    @Test
    void shouldCreateDeliveryCreatedEvent() {
        DeliveryCreatedEvent event =
                new DeliveryCreatedEvent(
                        "DELIVERY_CREATED", "2024-06-01T12:00:00Z", 1, "product-456");

        assertNotNull(event);
    }
}
