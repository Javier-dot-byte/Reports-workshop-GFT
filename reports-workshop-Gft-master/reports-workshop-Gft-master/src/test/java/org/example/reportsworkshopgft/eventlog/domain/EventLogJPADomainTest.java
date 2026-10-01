package org.example.reportsworkshopgft.eventlog.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.example.reportsworkshopgft.eventlog.domain.exception.EventLogIdNotUuidException;
import org.example.reportsworkshopgft.eventlog.domain.exception.InvalidEventLogIdException;
import org.example.reportsworkshopgft.eventlog.infrastructure.persistence.EventLogIdJPA;
import org.example.reportsworkshopgft.eventlog.infrastructure.persistence.EventLogJPA;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EventLogJPADomainTest {

    @Test
    void shouldGenerateAnIdentifier() {
        EventLogId generated = EventLogId.generate();

        assertNotNull(generated);
        assertNotNull(generated.value());
    }

    @Test
    void shouldRejectBlankIdentifierValues() {
        assertThrows(InvalidEventLogIdException.class, () -> new EventLogId(" "));
        assertThrows(InvalidEventLogIdException.class, () -> new EventLogId(null));
    }

    @Test
    void should_expose_value_via_getValue() {
        EventLogId id = new EventLogId("abc");
        assertEquals("abc", id.getValue());
    }

    @Test
    void should_convert_identifier_to_uuid() {
        UUID uuid = UUID.randomUUID();
        EventLogId id = new EventLogId(uuid.toString());

        assertEquals(uuid, id.toUUID());
    }

    @Test
    void should_throw_when_identifier_is_not_a_uuid() {
        EventLogId id = new EventLogId("not-a-uuid");

        assertThrows(EventLogIdNotUuidException.class, id::toUUID);
    }

    @Test
    void shouldExposeEventLogState() {
        EventLogIdJPA id = new EventLogIdJPA(UUID.randomUUID().toString());
        EventLogJPA eventLogJPA =
                new EventLogJPA(
                        id,
                        EventType.DELIVERY_CREATED,
                        SourceService.REPORTING,
                        "{\"payload\":true}",
                        7,
                        "2026-05-04T12:00:00");

        assertNotNull(eventLogJPA.getId());
        assertEquals(id.getValue(), eventLogJPA.getId().getValue());
        assertEquals(EventType.DELIVERY_CREATED, eventLogJPA.getEventType());
        assertEquals(SourceService.REPORTING, eventLogJPA.getSourceService());
        assertEquals("{\"payload\":true}", eventLogJPA.getPayload());
        assertEquals(7, eventLogJPA.getSimulationDay());
        assertEquals("2026-05-04T12:00:00", eventLogJPA.getOccurredAt());
    }

    @Test
    @DisplayName("Should expose available domain enums accurately")
    void shouldExposeAvailableDomainEnums() {
        // Comprobaciones seguras de EventType usando valueOf
        assertEquals(EventType.TIME_ADVANCED, EventType.valueOf("TIME_ADVANCED"));
        assertEquals(EventType.WAREHOUSE_STOCK_CHANGED, EventType.valueOf("WAREHOUSE_STOCK_CHANGED"));
        assertEquals(EventType.TRUCK_DELETED, EventType.valueOf("TRUCK_DELETED"));

        // Comprobaciones de SourceService
        assertEquals(SourceService.FACTORY, SourceService.valueOf("FACTORY"));
        assertEquals(SourceService.DELIVERY, SourceService.valueOf("DELIVERY"));
    }
}
