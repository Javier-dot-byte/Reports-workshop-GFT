package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.factory;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.reportsworkshopgft.eventlog.application.impl.EventLogServiceImpl;
import org.example.reportsworkshopgft.eventlog.domain.EventType;
import org.example.reportsworkshopgft.eventlog.domain.SourceService;
import org.example.reportsworkshopgft.eventlog.infrastructure.messaging.exception.EventProcessingException;
import org.example.reportsworkshopgft.eventlog.infrastructure.messaging.exception.EventSerializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class ProductionEventConsumerTest {

    @Mock private EventLogServiceImpl eventLogService;

    @Mock private ObjectMapper objectMapper;

    @InjectMocks private ProductionEventConsumer consumer;

    private final String DATE = "2026-05-12T10:00:00";
    private final String JSON_PAYLOAD = "{\"status\":\"serialized\"}";

    @BeforeEach
    void setUp() throws JsonProcessingException {
        lenient().when(objectMapper.writeValueAsString(any())).thenReturn(JSON_PAYLOAD);
    }

    @Test
    void should_save_on_ProductionOrderCreated() {
        var event = new ProductionOrderCreatedMessage("O1", "F1", "P1", 10, 5, DATE);

        consumer.onProductionOrderCreated(event);

        verify(eventLogService)
                .save(
                        EventType.PRODUCTION_ORDER_CREATED,
                        SourceService.FACTORY,
                        JSON_PAYLOAD,
                        5,
                        DATE);
    }

    @Test
    void should_save_on_ProductionOrderStarted() {
        var event = new ProductionOrderStartedMessage("O1", "F1", 5, DATE);

        consumer.onProductionOrderStarted(event);

        verify(eventLogService)
                .save(
                        EventType.PRODUCTION_ORDER_STARTED,
                        SourceService.FACTORY,
                        JSON_PAYLOAD,
                        5,
                        DATE);
    }

    @Test
    void should_save_on_ProductionOrderCompleted() {
        var event = new ProductionOrderCompletedMessage("O1", "F1", 5, DATE);

        consumer.onProductionOrderCompleted(event);

        verify(eventLogService)
                .save(
                        EventType.PRODUCTION_ORDER_COMPLETED,
                        SourceService.FACTORY,
                        JSON_PAYLOAD,
                        5,
                        DATE);
    }

    @Test
    void should_save_on_ProductionOrderBlocked() {
        var event = new ProductionOrderBlockedMessage(5, DATE);

        consumer.onProductionOrderBlocked(event);

        verify(eventLogService)
                .save(
                        EventType.PRODUCTION_ORDER_BLOCKED,
                        SourceService.FACTORY,
                        JSON_PAYLOAD,
                        5,
                        DATE);
    }

    @Test
    void should_save_on_RecipeRegistered() {
        var event = new ProductionRecipeRegisteredMessage(5, DATE);

        consumer.onRecipeRegistered(event);

        verify(eventLogService)
                .save(
                        EventType.PRODUCTION_RECIPE_REGISTERED,
                        SourceService.FACTORY,
                        JSON_PAYLOAD,
                        5,
                        DATE);
    }

    @Test
    void should_save_on_FactoryRegistered() {
        var event = new ProductionFactoryRegisteredMessage(5, DATE);

        consumer.onFactoryRegistered(event);

        verify(eventLogService)
                .save(
                        EventType.PRODUCTION_FACTORY_REGISTERED,
                        SourceService.FACTORY,
                        JSON_PAYLOAD,
                        5,
                        DATE);
    }

    @Test
    void should_throw_exception_when_onProductionOrderCreated_fails()
            throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("JSON Error"));
        var event = new ProductionOrderCreatedMessage("O1", "F1", "P1", 10, 5, DATE);

        assertThrows(
                EventProcessingException.class, () -> consumer.onProductionOrderCreated(event));
    }

    @Test
    void should_throw_exception_when_onProductionOrderStarted_fails()
            throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("JSON Error"));
        var event = new ProductionOrderStartedMessage("O1", "F1", 5, DATE);

        assertThrows(
                EventProcessingException.class, () -> consumer.onProductionOrderStarted(event));
    }

    @Test
    void should_throw_exception_when_onProductionOrderCompleted_fails()
            throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("JSON Error"));
        var event = new ProductionOrderCompletedMessage("O1", "F1", 5, DATE);

        assertThrows(
                EventProcessingException.class, () -> consumer.onProductionOrderCompleted(event));
    }

    @Test
    void should_throw_exception_when_onProductionOrderBlocked_fails()
            throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("JSON Error"));
        var event = new ProductionOrderBlockedMessage(5, DATE);

        assertThrows(
                EventProcessingException.class, () -> consumer.onProductionOrderBlocked(event));
    }

    @Test
    void should_throw_exception_when_onRecipeRegistered_fails() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("JSON Error"));
        var event = new ProductionRecipeRegisteredMessage(5, DATE);

        assertThrows(EventProcessingException.class, () -> consumer.onRecipeRegistered(event));
    }

    @Test
    void should_throw_exception_when_onFactoryRegistered_fails() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("JSON Error"));
        var event = new ProductionFactoryRegisteredMessage(5, DATE);

        assertThrows(EventProcessingException.class, () -> consumer.onFactoryRegistered(event));
    }

    @Test
    void should_handle_JsonProcessingException_in_onProductionOrderCreated()
            throws JsonProcessingException {
        var event = new ProductionOrderCreatedMessage("O1", "F1", "P1", 10, 5, DATE);
        when(objectMapper.writeValueAsString(eq(event)))
                .thenThrow(new com.fasterxml.jackson.databind.JsonMappingException("JSON Error"));

        assertThatThrownBy(() -> consumer.onProductionOrderCreated(event))
                .isInstanceOf(EventSerializationException.class);

        verify(eventLogService, never()).save(any(), any(), any(), anyInt(), any());
    }

    @Test
    void should_handle_DataAccessException_in_onProductionOrderCreated()
            throws JsonProcessingException {
        var event = new ProductionOrderCreatedMessage("O1", "F1", "P1", 10, 5, DATE);
        when(objectMapper.writeValueAsString(eq(event))).thenReturn(JSON_PAYLOAD);
        doThrow(new DataAccessException("DB Error") {})
                .when(eventLogService)
                .save(
                        eq(EventType.PRODUCTION_ORDER_CREATED),
                        eq(SourceService.FACTORY),
                        eq(JSON_PAYLOAD),
                        eq(5),
                        eq(DATE));

        assertThatThrownBy(() -> consumer.onProductionOrderCreated(event))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    void should_handle_JsonProcessingException_in_onProductionOrderStarted()
            throws JsonProcessingException {
        var event = new ProductionOrderStartedMessage("O1", "F1", 5, DATE);
        when(objectMapper.writeValueAsString(eq(event)))
                .thenThrow(new com.fasterxml.jackson.databind.JsonMappingException("JSON Error"));

        assertThatThrownBy(() -> consumer.onProductionOrderStarted(event))
                .isInstanceOf(EventSerializationException.class);

        verify(eventLogService, never()).save(any(), any(), any(), anyInt(), any());
    }

    @Test
    void should_handle_DataAccessException_in_onProductionOrderStarted()
            throws JsonProcessingException {
        var event = new ProductionOrderStartedMessage("O1", "F1", 5, DATE);
        when(objectMapper.writeValueAsString(eq(event))).thenReturn(JSON_PAYLOAD);
        doThrow(new DataAccessException("DB Error") {})
                .when(eventLogService)
                .save(
                        eq(EventType.PRODUCTION_ORDER_STARTED),
                        eq(SourceService.FACTORY),
                        eq(JSON_PAYLOAD),
                        eq(5),
                        eq(DATE));

        assertThatThrownBy(() -> consumer.onProductionOrderStarted(event))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    void should_handle_JsonProcessingException_in_onProductionOrderCompleted()
            throws JsonProcessingException {
        var event = new ProductionOrderCompletedMessage("O1", "F1", 5, DATE);
        when(objectMapper.writeValueAsString(eq(event)))
                .thenThrow(new com.fasterxml.jackson.databind.JsonMappingException("JSON Error"));

        assertThatThrownBy(() -> consumer.onProductionOrderCompleted(event))
                .isInstanceOf(EventSerializationException.class);

        verify(eventLogService, never()).save(any(), any(), any(), anyInt(), any());
    }

    @Test
    void should_handle_DataAccessException_in_onProductionOrderCompleted()
            throws JsonProcessingException {
        var event = new ProductionOrderCompletedMessage("O1", "F1", 5, DATE);
        when(objectMapper.writeValueAsString(eq(event))).thenReturn(JSON_PAYLOAD);
        doThrow(new DataAccessException("DB Error") {})
                .when(eventLogService)
                .save(
                        eq(EventType.PRODUCTION_ORDER_COMPLETED),
                        eq(SourceService.FACTORY),
                        eq(JSON_PAYLOAD),
                        eq(5),
                        eq(DATE));

        assertThatThrownBy(() -> consumer.onProductionOrderCompleted(event))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    void should_handle_JsonProcessingException_in_onProductionOrderBlocked()
            throws JsonProcessingException {
        var event = new ProductionOrderBlockedMessage(5, DATE);
        when(objectMapper.writeValueAsString(eq(event)))
                .thenThrow(new com.fasterxml.jackson.databind.JsonMappingException("JSON Error"));

        assertThatThrownBy(() -> consumer.onProductionOrderBlocked(event))
                .isInstanceOf(EventSerializationException.class);

        verify(eventLogService, never()).save(any(), any(), any(), anyInt(), any());
    }

    @Test
    void should_handle_DataAccessException_in_onProductionOrderBlocked()
            throws JsonProcessingException {
        var event = new ProductionOrderBlockedMessage(5, DATE);
        when(objectMapper.writeValueAsString(eq(event))).thenReturn(JSON_PAYLOAD);
        doThrow(new DataAccessException("DB Error") {})
                .when(eventLogService)
                .save(
                        eq(EventType.PRODUCTION_ORDER_BLOCKED),
                        eq(SourceService.FACTORY),
                        eq(JSON_PAYLOAD),
                        eq(5),
                        eq(DATE));

        assertThatThrownBy(() -> consumer.onProductionOrderBlocked(event))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    void should_handle_JsonProcessingException_in_onRecipeRegistered()
            throws JsonProcessingException {
        var event = new ProductionRecipeRegisteredMessage(5, DATE);
        when(objectMapper.writeValueAsString(eq(event)))
                .thenThrow(new com.fasterxml.jackson.databind.JsonMappingException("JSON Error"));

        assertThatThrownBy(() -> consumer.onRecipeRegistered(event))
                .isInstanceOf(EventSerializationException.class);

        verify(eventLogService, never()).save(any(), any(), any(), anyInt(), any());
    }

    @Test
    void should_handle_DataAccessException_in_onRecipeRegistered() throws JsonProcessingException {
        var event = new ProductionRecipeRegisteredMessage(5, DATE);
        when(objectMapper.writeValueAsString(eq(event))).thenReturn(JSON_PAYLOAD);
        doThrow(new DataAccessException("DB Error") {})
                .when(eventLogService)
                .save(
                        eq(EventType.PRODUCTION_RECIPE_REGISTERED),
                        eq(SourceService.FACTORY),
                        eq(JSON_PAYLOAD),
                        eq(5),
                        eq(DATE));

        assertThatThrownBy(() -> consumer.onRecipeRegistered(event))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    void should_handle_JsonProcessingException_in_onFactoryRegistered()
            throws JsonProcessingException {
        var event = new ProductionFactoryRegisteredMessage(5, DATE);
        when(objectMapper.writeValueAsString(eq(event)))
                .thenThrow(new com.fasterxml.jackson.databind.JsonMappingException("JSON Error"));

        assertThatThrownBy(() -> consumer.onFactoryRegistered(event))
                .isInstanceOf(EventSerializationException.class);

        verify(eventLogService, never()).save(any(), any(), any(), anyInt(), any());
    }

    @Test
    void should_handle_DataAccessException_in_onFactoryRegistered() throws JsonProcessingException {
        var event = new ProductionFactoryRegisteredMessage(5, DATE);
        when(objectMapper.writeValueAsString(eq(event))).thenReturn(JSON_PAYLOAD);
        doThrow(new DataAccessException("DB Error") {})
                .when(eventLogService)
                .save(
                        eq(EventType.PRODUCTION_FACTORY_REGISTERED),
                        eq(SourceService.FACTORY),
                        eq(JSON_PAYLOAD),
                        eq(5),
                        eq(DATE));

        assertThatThrownBy(() -> consumer.onFactoryRegistered(event))
                .isInstanceOf(DataAccessException.class);
    }
}
