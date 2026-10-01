package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.warehouse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.reportsworkshopgft.eventlog.application.impl.EventLogServiceImpl;
import org.example.reportsworkshopgft.eventlog.domain.EventType;
import org.example.reportsworkshopgft.eventlog.domain.SourceService;
import org.example.reportsworkshopgft.eventlog.infrastructure.messaging.exception.EventProcessingException;
import org.example.reportsworkshopgft.eventlog.infrastructure.messaging.exception.EventSerializationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataRetrievalFailureException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WarehouseEventConsumer Unit Tests - 100% Coverage Matrix")
class WarehouseEventConsumerTest {

    @Mock
    private EventLogServiceImpl eventLogService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private WarehouseEventConsumer warehouseEventConsumer;

    // Instancias de soporte falsas para simular la llegada de eventos de RabbitMQ
    private final WarehouseStockChangedMessage stockMessage = mock(WarehouseStockChangedMessage.class);
    private final ReplenishmentRequestedMessage replenishmentMessage = mock(ReplenishmentRequestedMessage.class);
    private final WarehouseOrderBlockedMessage orderBlockedMessage = mock(WarehouseOrderBlockedMessage.class);
    private final WarehouseRegisteredEvent registeredEvent = new WarehouseRegisteredEvent(UUID.randomUUID());

    private final String simulatedJson = "{\"status\":\"simulated\"}";

    @Nested
    @DisplayName("Scenario 1: Happy Path (Successful Processing)")
    class HappyPathTests {

        @Test
        @DisplayName("Should process WarehouseStockChanged successfully")
        void shouldProcessWarehouseStockChanged() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(stockMessage)).thenReturn(simulatedJson);

            warehouseEventConsumer.onWarehouseStockChanged(stockMessage);

            verify(eventLogService).save(EventType.WAREHOUSE_STOCK_CHANGED, SourceService.WAREHOUSE, simulatedJson, 0, "");
        }

        @Test
        @DisplayName("Should process ReplenishmentRequested successfully")
        void shouldProcessReplenishmentRequested() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(replenishmentMessage)).thenReturn(simulatedJson);

            warehouseEventConsumer.onReplenishmentRequested(replenishmentMessage);

            verify(eventLogService).save(EventType.REPLENISHMENT_REQUESTED, SourceService.WAREHOUSE, simulatedJson, 0, "");
        }

        @Test
        @DisplayName("Should process WarehouseOrderBlocked successfully")
        void shouldProcessWarehouseOrderBlocked() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(orderBlockedMessage)).thenReturn(simulatedJson);

            warehouseEventConsumer.onWarehouseOrderBlocked(orderBlockedMessage);

            verify(eventLogService).save(EventType.WAREHOUSE_ORDER_BLOCKED, SourceService.WAREHOUSE, simulatedJson, 0, "");
        }

        @Test
        @DisplayName("Should process WarehouseRegistered successfully")
        void shouldProcessWarehouseRegistered() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(registeredEvent)).thenReturn(simulatedJson);

            warehouseEventConsumer.onWarehouseRegistered(registeredEvent);

            verify(eventLogService).save(EventType.WAREHOUSE_REGISTERED, SourceService.WAREHOUSE, simulatedJson, 0, "");
        }
    }

    @Nested
    @DisplayName("Scenario 2: JsonProcessingException Branches")
    class SerializationErrorTests {

        @Test
        @DisplayName("onWarehouseStockChanged - Should throw EventSerializationException when Jackson fails")
        void stockChangedSerializationError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Simulated Error") {});

            assertThatThrownBy(() -> warehouseEventConsumer.onWarehouseStockChanged(stockMessage))
                    .isInstanceOf(EventSerializationException.class)
                    .hasMessageContaining("Error processing RabbitMQ event: warehouse.stock.changed.v1");
        }

        @Test
        @DisplayName("onReplenishmentRequested - Should throw EventSerializationException when Jackson fails")
        void replenishmentSerializationError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Simulated Error") {});

            assertThatThrownBy(() -> warehouseEventConsumer.onReplenishmentRequested(replenishmentMessage))
                    .isInstanceOf(EventSerializationException.class)
                    .hasMessageContaining("Error processing RabbitMQ event: replenishment.requested.v1");
        }

        @Test
        @DisplayName("onWarehouseOrderBlocked - Should throw EventSerializationException when Jackson fails")
        void orderBlockedSerializationError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Simulated Error") {});

            assertThatThrownBy(() -> warehouseEventConsumer.onWarehouseOrderBlocked(orderBlockedMessage))
                    .isInstanceOf(EventSerializationException.class)
                    .hasMessageContaining("Error processing RabbitMQ event: warehouse.order.blocked.v1");
        }

        @Test
        @DisplayName("onWarehouseRegistered - Should throw EventSerializationException when Jackson fails")
        void registeredSerializationError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Simulated Error") {});

            assertThatThrownBy(() -> warehouseEventConsumer.onWarehouseRegistered(registeredEvent))
                    .isInstanceOf(EventSerializationException.class)
                    .hasMessageContaining("Error processing RabbitMQ event: warehouse.registered.v1");
        }
    }

    @Nested
    @DisplayName("Scenario 3: DataAccessException Branches")
    class DatabaseErrorTests {

        @Test
        @DisplayName("onWarehouseStockChanged - Should rethrow DataAccessException directly")
        void stockChangedDatabaseError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(stockMessage)).thenReturn(simulatedJson);
            doThrow(new DataRetrievalFailureException("DB connection dead"))
                    .when(eventLogService).save(eq(EventType.WAREHOUSE_STOCK_CHANGED), any(), any(), anyInt(), anyString());

            assertThatThrownBy(() -> warehouseEventConsumer.onWarehouseStockChanged(stockMessage))
                    .isInstanceOf(DataRetrievalFailureException.class);
        }

        @Test
        @DisplayName("onReplenishmentRequested - Should rethrow DataAccessException directly")
        void replenishmentDatabaseError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(replenishmentMessage)).thenReturn(simulatedJson);
            doThrow(new DataRetrievalFailureException("DB connection dead"))
                    .when(eventLogService).save(eq(EventType.REPLENISHMENT_REQUESTED), any(), any(), anyInt(), anyString());

            assertThatThrownBy(() -> warehouseEventConsumer.onReplenishmentRequested(replenishmentMessage))
                    .isInstanceOf(DataRetrievalFailureException.class);
        }

        @Test
        @DisplayName("onWarehouseOrderBlocked - Should rethrow DataAccessException directly")
        void orderBlockedDatabaseError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(orderBlockedMessage)).thenReturn(simulatedJson);
            doThrow(new DataRetrievalFailureException("DB connection dead"))
                    .when(eventLogService).save(eq(EventType.WAREHOUSE_ORDER_BLOCKED), any(), any(), anyInt(), anyString());

            assertThatThrownBy(() -> warehouseEventConsumer.onWarehouseOrderBlocked(orderBlockedMessage))
                    .isInstanceOf(DataRetrievalFailureException.class);
        }

        @Test
        @DisplayName("onWarehouseRegistered - Should rethrow DataAccessException directly")
        void registeredDatabaseError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(registeredEvent)).thenReturn(simulatedJson);
            doThrow(new DataRetrievalFailureException("DB connection dead"))
                    .when(eventLogService).save(eq(EventType.WAREHOUSE_REGISTERED), any(), any(), anyInt(), anyString());

            assertThatThrownBy(() -> warehouseEventConsumer.onWarehouseRegistered(registeredEvent))
                    .isInstanceOf(DataRetrievalFailureException.class);
        }
    }

    @Nested
    @DisplayName("Scenario 4: RuntimeException (Unexpected) Branches")
    class UnexpectedErrorTests {

        @Test
        @DisplayName("onWarehouseStockChanged - Should wrap unexpected exceptions into EventProcessingException")
        void stockChangedUnexpectedError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(stockMessage)).thenReturn(simulatedJson);
            doThrow(new NullPointerException("Unexpected null reference"))
                    .when(eventLogService).save(eq(EventType.WAREHOUSE_STOCK_CHANGED), any(), any(), anyInt(), anyString());

            assertThatThrownBy(() -> warehouseEventConsumer.onWarehouseStockChanged(stockMessage))
                    .isInstanceOf(EventProcessingException.class)
                    .hasMessageContaining("Error processing RabbitMQ event: warehouse.stock.changed.v1");
        }

        @Test
        @DisplayName("onReplenishmentRequested - Should wrap unexpected exceptions into EventProcessingException")
        void replenishmentUnexpectedError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(replenishmentMessage)).thenReturn(simulatedJson);
            doThrow(new NullPointerException("Unexpected null reference"))
                    .when(eventLogService).save(eq(EventType.REPLENISHMENT_REQUESTED), any(), any(), anyInt(), anyString());

            assertThatThrownBy(() -> warehouseEventConsumer.onReplenishmentRequested(replenishmentMessage))
                    .isInstanceOf(EventProcessingException.class)
                    .hasMessageContaining("Error processing RabbitMQ event: replenishment.requested.v1");
        }

        @Test
        @DisplayName("onWarehouseOrderBlocked - Should wrap unexpected exceptions into EventProcessingException")
        void orderBlockedUnexpectedError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(orderBlockedMessage)).thenReturn(simulatedJson);
            doThrow(new NullPointerException("Unexpected null reference"))
                    .when(eventLogService).save(eq(EventType.WAREHOUSE_ORDER_BLOCKED), any(), any(), anyInt(), anyString());

            assertThatThrownBy(() -> warehouseEventConsumer.onWarehouseOrderBlocked(orderBlockedMessage))
                    .isInstanceOf(EventProcessingException.class)
                    .hasMessageContaining("Error processing RabbitMQ event: warehouse.order.blocked.v1");
        }

        @Test
        @DisplayName("onWarehouseRegistered - Should wrap unexpected exceptions into EventProcessingException")
        void registeredUnexpectedError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(registeredEvent)).thenReturn(simulatedJson);
            doThrow(new NullPointerException("Unexpected null reference"))
                    .when(eventLogService).save(eq(EventType.WAREHOUSE_REGISTERED), any(), any(), anyInt(), anyString());

            assertThatThrownBy(() -> warehouseEventConsumer.onWarehouseRegistered(registeredEvent))
                    .isInstanceOf(EventProcessingException.class)
                    .hasMessageContaining("Error processing RabbitMQ event: warehouse.registered.v1");
        }
    }
}