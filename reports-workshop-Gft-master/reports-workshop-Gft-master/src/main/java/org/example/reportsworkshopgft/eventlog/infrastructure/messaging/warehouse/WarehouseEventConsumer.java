package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.warehouse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.reportsworkshopgft.eventlog.application.impl.EventLogServiceImpl;
import org.example.reportsworkshopgft.eventlog.domain.EventType;
import org.example.reportsworkshopgft.eventlog.domain.SourceService;
import org.example.reportsworkshopgft.eventlog.infrastructure.messaging.exception.EventProcessingException;
import org.example.reportsworkshopgft.eventlog.infrastructure.messaging.exception.EventSerializationException;
import org.example.reportsworkshopgft.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseEventConsumer {

    private final EventLogServiceImpl eventLogService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.WAREHOUSE_STOCK_CHANGED_QUEUE_NAME)
    public void onWarehouseStockChanged(WarehouseStockChangedMessage event) {
        final String eventName = "warehouse.stock.changed.v1";
        try {
            log.info("Warehouse event received: {}", event);
            String jsonPayload = objectMapper.writeValueAsString(event);
            eventLogService.save(
                    EventType.WAREHOUSE_STOCK_CHANGED, SourceService.WAREHOUSE, jsonPayload, 0, "");
        } catch (JsonProcessingException e) {
            log.error("Error serializing event to JSON: {}", event, e);
            throw new EventSerializationException(
                    eventName, "Error processing RabbitMQ event: " + eventName, e);
        } catch (DataAccessException e) {
            log.error("Database error while saving log: {}", event, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error processing warehouse event: {}", event, e);
            throw new EventProcessingException(
                    eventName, "Error processing RabbitMQ event: " + eventName, e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.REPLENISHMENT_REQUESTED_QUEUE_NAME)
    public void onReplenishmentRequested(ReplenishmentRequestedMessage event) {
        final String eventName = "replenishment.requested.v1";
        try {
            log.info("Warehouse event received: {}", event);
            String jsonPayload = objectMapper.writeValueAsString(event);
            eventLogService.save(
                    EventType.REPLENISHMENT_REQUESTED, SourceService.WAREHOUSE, jsonPayload, 0, "");
        } catch (JsonProcessingException e) {
            log.error("Error serializing event to JSON: {}", event, e);
            throw new EventSerializationException(
                    eventName, "Error processing RabbitMQ event: " + eventName, e);
        } catch (DataAccessException e) {
            log.error("Database error while saving log: {}", event, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error processing warehouse event: {}", event, e);
            throw new EventProcessingException(
                    eventName, "Error processing RabbitMQ event: " + eventName, e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.WAREHOUSE_ORDER_BLOCKED_QUEUE_NAME)
    public void onWarehouseOrderBlocked(WarehouseOrderBlockedMessage event) {
        final String eventName = "warehouse.order.blocked.v1";
        try {
            log.info("Warehouse event received: {}", event);
            String jsonPayload = objectMapper.writeValueAsString(event);
            eventLogService.save(
                    EventType.WAREHOUSE_ORDER_BLOCKED, SourceService.WAREHOUSE, jsonPayload, 0, "");
        } catch (JsonProcessingException e) {
            log.error("Error serializing event to JSON: {}", event, e);
            throw new EventSerializationException(
                    eventName, "Error processing RabbitMQ event: " + eventName, e);
        } catch (DataAccessException e) {
            log.error("Database error while saving log: {}", event, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error processing warehouse event: {}", event, e);
            throw new EventProcessingException(
                    eventName, "Error processing RabbitMQ event: " + eventName, e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.WAREHOUSE_REGISTERED_QUEUE_NAME)
    public void onWarehouseRegistered(WarehouseRegisteredEvent event) {
        final String eventName = "warehouse.registered.v1";
        try {
            log.info("Warehouse event received: {}", event);
            String jsonPayload = objectMapper.writeValueAsString(event);
            eventLogService.save(
                    EventType.WAREHOUSE_REGISTERED, SourceService.WAREHOUSE, jsonPayload, 0, "");
        } catch (JsonProcessingException e) {
            log.error("Error serializing event to JSON: {}", event, e);
            throw new EventSerializationException(
                    eventName, "Error processing RabbitMQ event: " + eventName, e);
        } catch (DataAccessException e) {
            log.error("Database error while saving log: {}", event, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error processing warehouse event: {}", event, e);
            throw new EventProcessingException(
                    eventName, "Error processing RabbitMQ event: " + eventName, e);
        }
    }
}
