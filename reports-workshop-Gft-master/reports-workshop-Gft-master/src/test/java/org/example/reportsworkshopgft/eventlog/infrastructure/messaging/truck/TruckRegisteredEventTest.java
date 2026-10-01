package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.truck;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class TruckRegisteredEventTest {

    @Test
    void should_exercise_all_record_methods_for_coverage() {

        UUID truckId = UUID.randomUUID();
        var position = new TruckRegisteredEvent.Position(10, 20);
        var event1 = new TruckRegisteredEvent(truckId, "Truck 1", position, 100, 12345);
        var event2 = new TruckRegisteredEvent(truckId, "Truck 1", position, 100, 12345);
        var event3 = new TruckRegisteredEvent(UUID.randomUUID(), "Truck 2", position, 50, 67890);

        assertThat(event1.truckId()).isEqualTo(truckId);
        assertThat(event1.name()).isEqualTo("Truck 1");
        assertThat(event1.position()).isEqualTo(position);
        assertThat(event1.capacity()).isEqualTo(100);
        assertThat(event1.timestamp()).isEqualTo(12345);

        assertThat(position.x()).isEqualTo(10);
        assertThat(position.y()).isEqualTo(20);

        assertThat(event1.toString()).contains("truckId=" + truckId);
        assertThat(position.toString()).contains("x=10");

        assertThat(event1.hashCode()).isEqualTo(event2.hashCode());
        assertThat(event1.hashCode()).isNotEqualTo(event3.hashCode());

        assertThat(event1).isEqualTo(event1);

        assertThat(event1).isEqualTo(event2);

        assertThat(event1).isNotEqualTo(event3);

        assertThat(event1.equals(null)).isFalse();
        assertThat(event1.equals("no soy un evento")).isFalse();

        assertThat(position).isEqualTo(new TruckRegisteredEvent.Position(10, 20));
        assertThat(position).isNotEqualTo(new TruckRegisteredEvent.Position(0, 0));
    }
}
