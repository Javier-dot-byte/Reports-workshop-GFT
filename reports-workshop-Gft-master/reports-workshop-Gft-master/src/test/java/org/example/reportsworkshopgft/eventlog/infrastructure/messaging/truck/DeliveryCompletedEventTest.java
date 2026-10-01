package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.truck;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DeliveryCompletedEventTest {

    @Test
    void should_create_delivery_completed_event_with_all_fields() {
        DeliveryCompletedEvent event =
                new DeliveryCompletedEvent("DEL-01", "TRUCK-01", 3, "2026-05-13T12:00:00");

        assertThat(event.deliveryId()).isEqualTo("DEL-01");
        assertThat(event.truckId()).isEqualTo("TRUCK-01");
        assertThat(event.simulationDay()).isEqualTo(3);
        assertThat(event.timestamp()).isEqualTo("2026-05-13T12:00:00");
    }
}
