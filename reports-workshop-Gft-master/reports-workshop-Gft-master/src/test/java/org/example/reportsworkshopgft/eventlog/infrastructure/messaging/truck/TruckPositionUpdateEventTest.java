package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.truck;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TruckPositionUpdateEventTest {

    @Test
    void coverage_test() {
        var pos = new TruckPositionUpdateEvent.Position(10, 20);
        var event = new TruckPositionUpdateEvent("T1", pos, 1, "2023-10-01");

        assertThat(event.truckId()).isEqualTo("T1");
        assertThat(event.position()).isEqualTo(pos);
        assertThat(event.simulationDay()).isEqualTo(1);
        assertThat(event.timestamp()).isEqualTo("2023-10-01");
        assertThat(pos.x()).isEqualTo(10);
        assertThat(pos.y()).isEqualTo(20);

        var sameEvent = new TruckPositionUpdateEvent("T1", pos, 1, "2023-10-01");
        assertThat(event).isEqualTo(sameEvent);
        assertThat(event.hashCode()).isEqualTo(sameEvent.hashCode());
        assertThat(event).isNotEqualTo(null);
        assertThat(event).isNotEqualTo("String");

        assertThat(event.toString()).contains("T1");
        assertThat(pos.toString()).contains("10");
    }
}
