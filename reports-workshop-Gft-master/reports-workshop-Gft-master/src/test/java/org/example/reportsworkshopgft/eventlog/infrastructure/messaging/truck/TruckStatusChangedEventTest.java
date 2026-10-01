package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.truck;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TruckStatusChangedEventTest {

    @Test
    void should_create_truck_status_changed_event_with_all_fields() {
        TruckStatusChangedEvent event =
                new TruckStatusChangedEvent("TRUCK-01", "IN_TRANSIT", 2, "2026-05-13T12:00:00");

        assertThat(event.truckId()).isEqualTo("TRUCK-01");
        assertThat(event.status()).isEqualTo("IN_TRANSIT");
        assertThat(event.simulationDay()).isEqualTo(2);
        assertThat(event.timestamp()).isEqualTo("2026-05-13T12:00:00");
    }
}
