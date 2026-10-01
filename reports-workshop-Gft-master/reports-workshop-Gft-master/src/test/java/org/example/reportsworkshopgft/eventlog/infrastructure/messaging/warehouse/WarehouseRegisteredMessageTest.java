package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.warehouse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit Tests - WarehouseRegisteredEvent")
class WarehouseRegisteredEventTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Given a valid JSON, when deserialized, then fields must be mapped correctly")
    void shouldDeserializeCorrectly() throws JsonProcessingException {
        UUID warehouseId = UUID.randomUUID();
        String json = String.format("{\"warehouseId\":\"%s\"}", warehouseId);

        WarehouseRegisteredEvent event = objectMapper.readValue(json, WarehouseRegisteredEvent.class);

        assertThat(event).isNotNull();
        assertThat(event.warehouseId()).isEqualTo(warehouseId);
    }

    @Test
    @DisplayName("Given an event instance, when serialized, then JSON properties must match annotations")
    void shouldSerializeCorrectly() throws JsonProcessingException {
        UUID warehouseId = UUID.randomUUID();
        WarehouseRegisteredEvent event = new WarehouseRegisteredEvent(warehouseId);

        String jsonResult = objectMapper.writeValueAsString(event);

        assertThat(jsonResult).contains("\"warehouseId\":\"" + warehouseId + "\"");
    }

    @Test
    @DisplayName("Given two identical events, then they must be equal and share the same hashCode")
    void shouldVerifyEqualsAndHashCodeContract() {
        UUID sameId = UUID.randomUUID();
        WarehouseRegisteredEvent eventA = new WarehouseRegisteredEvent(sameId);
        WarehouseRegisteredEvent eventB = new WarehouseRegisteredEvent(sameId);
        WarehouseRegisteredEvent eventC = new WarehouseRegisteredEvent(UUID.randomUUID());

        assertThat(eventA).isEqualTo(eventB);
        assertThat(eventA.hashCode()).isEqualTo(eventB.hashCode());

        assertThat(eventA).isNotEqualTo(eventC);
        assertThat(eventA).isNotEqualTo(null);
        assertThat(eventA).isNotEqualTo(new Object());

        assertThat(eventA.toString()).contains("warehouseId=" + sameId);
    }
}