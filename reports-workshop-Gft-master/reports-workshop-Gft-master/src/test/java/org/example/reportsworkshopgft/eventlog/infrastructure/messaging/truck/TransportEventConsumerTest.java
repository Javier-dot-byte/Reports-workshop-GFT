package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.truck;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

@ExtendWith(MockitoExtension.class)
@DisplayName("TransportEventConsumer Unit Tests - 100% Coverage Matrix")
class TransportEventConsumerTest {

    @Mock private EventLogServiceImpl eventLogService;

    @Mock private ObjectMapper objectMapper;

    @InjectMocks private TransportEventConsumer transportEventConsumer;

    private final TruckRegisteredEvent registeredEvent = mock(TruckRegisteredEvent.class);
    private final TruckPositionUpdateEvent positionUpdateEvent =
            mock(TruckPositionUpdateEvent.class);
    private final TruckStatusChangedEvent statusChangedEvent = mock(TruckStatusChangedEvent.class);
    private final DeliveryCompletedEvent deliveryCompletedEvent =
            mock(DeliveryCompletedEvent.class);
    private final TruckDeletedEvent truckDeletedEvent =
            new TruckDeletedEvent("DEL-12", "TRUCK-01", 3, "2026-05-13T12:00:00");

    private final String simulatedJson = "{\"status\":\"processed\"}";

    @Nested
    @DisplayName("Scenario 1: Happy Paths")
    class HappyPathTests {

        @Test
        @DisplayName("Should process TruckRegisteredEvent successfully")
        void shouldProcessTruckRegistered() throws JsonProcessingException {
            when(registeredEvent.timestamp()).thenReturn(1);
            when(objectMapper.writeValueAsString(registeredEvent)).thenReturn(simulatedJson);

            transportEventConsumer.onTruckRegistered(registeredEvent);

            verify(eventLogService)
                    .save(
                            eq(EventType.TRUCK_REGISTERED),
                            eq(SourceService.TRANSPORT),
                            eq(simulatedJson),
                            eq(1),
                            anyString());
        }

        @Test
        @DisplayName("Should process TruckPositionUpdateEvent successfully")
        void shouldProcessTruckPositionUpdate() throws JsonProcessingException {
            when(positionUpdateEvent.simulationDay()).thenReturn(5);
            when(positionUpdateEvent.timestamp()).thenReturn("2026-05-13T12:10:00");
            when(objectMapper.writeValueAsString(positionUpdateEvent)).thenReturn(simulatedJson);

            transportEventConsumer.onTruckPositionUpdate(positionUpdateEvent);

            verify(eventLogService)
                    .save(
                            EventType.TRUCK_POSITION_UPDATED,
                            SourceService.TRANSPORT,
                            simulatedJson,
                            5,
                            "2026-05-13T12:10:00");
        }

        @Test
        @DisplayName("Should process TruckStatusChangedEvent successfully")
        void shouldProcessTruckStatusChanged() throws JsonProcessingException {
            when(statusChangedEvent.simulationDay()).thenReturn(2);
            when(statusChangedEvent.timestamp()).thenReturn("2026-05-13T12:20:00");
            when(objectMapper.writeValueAsString(statusChangedEvent)).thenReturn(simulatedJson);

            transportEventConsumer.onTruckStatusChanged(statusChangedEvent);

            verify(eventLogService)
                    .save(
                            EventType.TRUCK_STATUS_CHANGED,
                            SourceService.TRANSPORT,
                            simulatedJson,
                            2,
                            "2026-05-13T12:20:00");
        }

        @Test
        @DisplayName("Should process DeliveryCompletedEvent saving two logs")
        void shouldProcessDeliveryCompleted() throws JsonProcessingException {
            when(deliveryCompletedEvent.simulationDay()).thenReturn(4);
            when(deliveryCompletedEvent.timestamp()).thenReturn("2026-05-13T12:30:00");
            when(objectMapper.writeValueAsString(deliveryCompletedEvent)).thenReturn(simulatedJson);

            transportEventConsumer.onDeliveryCompleted(deliveryCompletedEvent);

            verify(eventLogService)
                    .save(
                            EventType.DELIVERY_COMPLETED,
                            SourceService.TRANSPORT,
                            simulatedJson,
                            4,
                            "2026-05-13T12:30:00");
            verify(eventLogService)
                    .save(
                            EventType.TRUCK_STATUS_CHANGED,
                            SourceService.TRANSPORT,
                            simulatedJson,
                            4,
                            "2026-05-13T12:30:00");
        }

        @Test
        @DisplayName("Should process TruckDeletedEvent successfully")
        void shouldProcessTruckDeleted() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(truckDeletedEvent)).thenReturn(simulatedJson);

            transportEventConsumer.onTruckDeleted(truckDeletedEvent);

            verify(eventLogService)
                    .save(
                            EventType.TRUCK_DELETED,
                            SourceService.TRANSPORT,
                            simulatedJson,
                            3,
                            "2026-05-13T12:00:00");
        }
    }

    @Nested
    @DisplayName("Scenario 2: JsonProcessingException Branches")
    class SerializationErrorTests {

        @Test
        @DisplayName("onTruckRegistered - Should throw EventSerializationException")
        void registeredSerializationError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(any()))
                    .thenThrow(new JsonProcessingException("Fail") {});

            assertThatThrownBy(() -> transportEventConsumer.onTruckRegistered(registeredEvent))
                    .isInstanceOf(EventSerializationException.class);
        }

        @Test
        @DisplayName("onTruckPositionUpdate - Should throw EventSerializationException")
        void positionSerializationError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(any()))
                    .thenThrow(new JsonProcessingException("Fail") {});

            assertThatThrownBy(
                            () -> transportEventConsumer.onTruckPositionUpdate(positionUpdateEvent))
                    .isInstanceOf(EventSerializationException.class);
        }

        @Test
        @DisplayName("onTruckStatusChanged - Should throw EventSerializationException")
        void statusSerializationError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(any()))
                    .thenThrow(new JsonProcessingException("Fail") {});

            assertThatThrownBy(
                            () -> transportEventConsumer.onTruckStatusChanged(statusChangedEvent))
                    .isInstanceOf(EventSerializationException.class);
        }

        @Test
        @DisplayName("onDeliveryCompleted - Should throw EventSerializationException")
        void deliverySerializationError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(any()))
                    .thenThrow(new JsonProcessingException("Fail") {});

            assertThatThrownBy(
                            () ->
                                    transportEventConsumer.onDeliveryCompleted(
                                            deliveryCompletedEvent))
                    .isInstanceOf(EventSerializationException.class);
        }
    }

    @Nested
    @DisplayName("Scenario 3: DataAccessException Branches")
    class DatabaseErrorTests {

        @Test
        @DisplayName("onTruckRegistered - Should rethrow DataAccessException directly")
        void registeredDatabaseError() throws JsonProcessingException {
            when(registeredEvent.timestamp()).thenReturn(1);
            when(objectMapper.writeValueAsString(registeredEvent)).thenReturn(simulatedJson);
            doThrow(new DataRetrievalFailureException("DB Error"))
                    .when(eventLogService)
                    .save(any(), any(), any(), any(), any());

            assertThatThrownBy(() -> transportEventConsumer.onTruckRegistered(registeredEvent))
                    .isInstanceOf(DataRetrievalFailureException.class);
        }

        @Test
        @DisplayName("onTruckPositionUpdate - Should rethrow DataAccessException directly")
        void positionDatabaseError() throws JsonProcessingException {
            when(positionUpdateEvent.simulationDay()).thenReturn(5);
            when(objectMapper.writeValueAsString(positionUpdateEvent)).thenReturn(simulatedJson);
            doThrow(new DataRetrievalFailureException("DB Error"))
                    .when(eventLogService)
                    .save(any(), any(), any(), any(), any());

            assertThatThrownBy(
                            () -> transportEventConsumer.onTruckPositionUpdate(positionUpdateEvent))
                    .isInstanceOf(DataRetrievalFailureException.class);
        }

        @Test
        @DisplayName("onTruckStatusChanged - Should rethrow DataAccessException directly")
        void statusDatabaseError() throws JsonProcessingException {
            when(statusChangedEvent.simulationDay()).thenReturn(2);
            when(statusChangedEvent.timestamp()).thenReturn("2026-05-13T12:20:00");
            when(objectMapper.writeValueAsString(statusChangedEvent)).thenReturn(simulatedJson);
            doThrow(new DataRetrievalFailureException("DB Error"))
                    .when(eventLogService)
                    .save(any(), any(), any(), anyInt(), anyString());

            assertThatThrownBy(
                            () -> transportEventConsumer.onTruckStatusChanged(statusChangedEvent))
                    .isInstanceOf(DataRetrievalFailureException.class);
        }

        @Test
        @DisplayName("onDeliveryCompleted - Should rethrow DataAccessException directly")
        void deliveryDatabaseError() throws JsonProcessingException {
            when(deliveryCompletedEvent.simulationDay()).thenReturn(4);
            when(deliveryCompletedEvent.timestamp()).thenReturn("2026-05-13T12:30:00");
            when(objectMapper.writeValueAsString(deliveryCompletedEvent)).thenReturn(simulatedJson);
            doThrow(new DataRetrievalFailureException("DB Error"))
                    .when(eventLogService)
                    .save(any(), any(), any(), anyInt(), anyString());

            assertThatThrownBy(
                            () ->
                                    transportEventConsumer.onDeliveryCompleted(
                                            deliveryCompletedEvent))
                    .isInstanceOf(DataRetrievalFailureException.class);
        }
    }

    @Nested
    @DisplayName("Scenario 4: RuntimeException Branches")
    class UnexpectedErrorTests {

        @Test
        @DisplayName("onTruckRegistered - Should wrap into EventProcessingException")
        void registeredUnexpectedError() throws JsonProcessingException {
            when(registeredEvent.timestamp()).thenReturn(1);
            when(objectMapper.writeValueAsString(registeredEvent)).thenReturn(simulatedJson);
            doThrow(new NullPointerException("Crash"))
                    .when(eventLogService)
                    .save(any(), any(), any(), any(), any());

            assertThatThrownBy(() -> transportEventConsumer.onTruckRegistered(registeredEvent))
                    .isInstanceOf(EventProcessingException.class);
        }

        @Test
        @DisplayName("onTruckPositionUpdate - Should wrap into EventProcessingException")
        void positionUnexpectedError() throws JsonProcessingException {
            when(positionUpdateEvent.simulationDay()).thenReturn(0);
            when(positionUpdateEvent.timestamp()).thenReturn("2026-05-13T12:10:00");
            when(objectMapper.writeValueAsString(positionUpdateEvent)).thenReturn(simulatedJson);
            doThrow(new NullPointerException("Crash"))
                    .when(eventLogService)
                    .save(any(), any(), any(), anyInt(), anyString());

            assertThatThrownBy(
                            () -> transportEventConsumer.onTruckPositionUpdate(positionUpdateEvent))
                    .isInstanceOf(EventProcessingException.class);
        }

        @Test
        @DisplayName("onTruckStatusChanged - Should wrap into EventProcessingException")
        void statusUnexpectedError() throws JsonProcessingException {
            when(statusChangedEvent.simulationDay()).thenReturn(0);
            when(statusChangedEvent.timestamp()).thenReturn("2026-05-13T12:20:00");
            when(objectMapper.writeValueAsString(statusChangedEvent)).thenReturn(simulatedJson);
            doThrow(new NullPointerException("Crash"))
                    .when(eventLogService)
                    .save(any(), any(), any(), anyInt(), anyString());

            assertThatThrownBy(
                            () -> transportEventConsumer.onTruckStatusChanged(statusChangedEvent))
                    .isInstanceOf(EventProcessingException.class);
        }

        @Test
        @DisplayName("onDeliveryCompleted - Should wrap into EventProcessingException")
        void deliveryUnexpectedError() throws JsonProcessingException {
            when(deliveryCompletedEvent.simulationDay()).thenReturn(4);
            when(deliveryCompletedEvent.timestamp()).thenReturn("2026-05-13T12:30:00");
            when(objectMapper.writeValueAsString(deliveryCompletedEvent)).thenReturn(simulatedJson);
            doThrow(new NullPointerException("Crash"))
                    .when(eventLogService)
                    .save(any(), any(), any(), anyInt(), anyString());

            assertThatThrownBy(
                            () ->
                                    transportEventConsumer.onDeliveryCompleted(
                                            deliveryCompletedEvent))
                    .isInstanceOf(EventProcessingException.class);
        }

        @Test
        @DisplayName("onTruckDeleted - Should wrap into EventProcessingException")
        void deletedUnexpectedError() throws JsonProcessingException {
            when(objectMapper.writeValueAsString(any()))
                    .thenThrow(new RuntimeException("General Crash"));

            assertThatThrownBy(() -> transportEventConsumer.onTruckDeleted(truckDeletedEvent))
                    .isInstanceOf(EventProcessingException.class);
        }
    }
}
