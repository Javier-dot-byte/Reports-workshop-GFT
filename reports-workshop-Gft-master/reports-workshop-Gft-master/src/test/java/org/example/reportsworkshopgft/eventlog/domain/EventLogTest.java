package org.example.reportsworkshopgft.eventlog.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class EventLogTest {

    @Test
    void shouldCompareEntitiesByValue() {
        EventLogId id = EventLogId.generate();
        EventLog left =
                EventLog.builder()
                        .id(id)
                        .eventType(EventType.TRUCK_REGISTERED)
                        .sourceService(SourceService.TRANSPORT)
                        .payload("{\"truckId\":\"1\"}")
                        .simulationDay(2)
                        .occurredAt("2026-05-07T18:00:00")
                        .build();
        EventLog right = new EventLog();
        right.setId(id);
        right.setEventType(EventType.TRUCK_REGISTERED);
        right.setSourceService(SourceService.TRANSPORT);
        right.setPayload("{\"truckId\":\"1\"}");
        right.setSimulationDay(2);
        right.setOccurredAt("2026-05-07T18:00:00");

        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());

        right.setPayload("{\"truckId\":\"2\"}");

        assertNotEquals(left, right);
    }
}
