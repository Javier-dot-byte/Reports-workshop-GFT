package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.factory;

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
public class ProductionEventConsumer {

    private final EventLogServiceImpl eventLogService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.PRODUCTION_ORDER_CREATED_QUEUE_NAME)
    public void onProductionOrderCreated(ProductionOrderCreatedMessage event) {
        final String eventName = RabbitMQConfig.PRODUCTION_ORDER_CREATED_ROUTING_KEY;
        try {
            log.info("Production event received: {}", event);

            String jsonPayload = objectMapper.writeValueAsString(event);

            eventLogService.save(
                    EventType.PRODUCTION_ORDER_CREATED,
                    SourceService.FACTORY,
                    jsonPayload,
                    event.simulationDay(),
                    event.occurredAt());
        } catch (JsonProcessingException e) {
            log.error("Error serializing event to JSON: {}", event, e);
            throw new EventSerializationException(eventName, "Error processing " + eventName, e);
        } catch (DataAccessException e) {
            log.error("Database error while saving log: {}", event, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error processing production order: {}", event, e);
            throw new EventProcessingException(eventName, "Error processing " + eventName, e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.PRODUCTION_ORDER_STARTED_QUEUE_NAME)
    public void onProductionOrderStarted(ProductionOrderStartedMessage event) {
        final String eventName = RabbitMQConfig.PRODUCTION_ORDER_STARTED_ROUTING_KEY;
        try {
            log.info("Production event received: {}", event);

            String jsonPayload = objectMapper.writeValueAsString(event);

            eventLogService.save(
                    EventType.PRODUCTION_ORDER_STARTED,
                    SourceService.FACTORY,
                    jsonPayload,
                    event.simulationDay(),
                    event.occurredAt());
        } catch (JsonProcessingException e) {
            log.error("Error serializing event to JSON: {}", event, e);
            throw new EventSerializationException(eventName, "Error processing " + eventName, e);
        } catch (DataAccessException e) {
            log.error("Database error while saving log: {}", event, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error processing production order: {}", event, e);
            throw new EventProcessingException(eventName, "Error processing " + eventName, e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.PRODUCTION_ORDER_COMPLETED_QUEUE_NAME)
    public void onProductionOrderCompleted(ProductionOrderCompletedMessage event) {
        final String eventName = RabbitMQConfig.PRODUCTION_ORDER_COMPLETED_ROUTING_KEY;
        try {
            log.info("Production event received: {}", event);

            String jsonPayload = objectMapper.writeValueAsString(event);

            eventLogService.save(
                    EventType.PRODUCTION_ORDER_COMPLETED,
                    SourceService.FACTORY,
                    jsonPayload,
                    event.simulationDay(),
                    event.occurredAt());
        } catch (JsonProcessingException e) {
            log.error("Error serializing event to JSON: {}", event, e);
            throw new EventSerializationException(eventName, "Error processing " + eventName, e);
        } catch (DataAccessException e) {
            log.error("Database error while saving log: {}", event, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error processing production order: {}", event, e);
            throw new EventProcessingException(eventName, "Error processing " + eventName, e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.PRODUCTION_ORDER_BLOCKED_QUEUE_NAME)
    public void onProductionOrderBlocked(ProductionOrderBlockedMessage event) {
        final String eventName = RabbitMQConfig.PRODUCTION_ORDER_BLOCKED_ROUTING_KEY;
        try {
            log.info("Production event received: {}", event);

            String jsonPayload = objectMapper.writeValueAsString(event);

            eventLogService.save(
                    EventType.PRODUCTION_ORDER_BLOCKED,
                    SourceService.FACTORY,
                    jsonPayload,
                    event.simulationDay(),
                    event.occurredAt());
        } catch (JsonProcessingException e) {
            log.error("Error serializing event to JSON: {}", event, e);
            throw new EventSerializationException(eventName, "Error processing " + eventName, e);
        } catch (DataAccessException e) {
            log.error("Database error while saving log: {}", event, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error processing production order: {}", event, e);
            throw new EventProcessingException(eventName, "Error processing " + eventName, e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.RECIPE_REGISTERED_QUEUE_NAME)
    public void onRecipeRegistered(ProductionRecipeRegisteredMessage event) {
        final String eventName = RabbitMQConfig.RECIPE_REGISTERED_ROUTING_KEY;
        try {
            log.info("Production event received: {}", event);

            String jsonPayload = objectMapper.writeValueAsString(event);

            eventLogService.save(
                    EventType.PRODUCTION_RECIPE_REGISTERED,
                    SourceService.FACTORY,
                    jsonPayload,
                    event.simulationDay(),
                    event.occurredAt());
        } catch (JsonProcessingException e) {
            log.error("Error serializing event to JSON: {}", event, e);
            throw new EventSerializationException(eventName, "Error processing " + eventName, e);
        } catch (DataAccessException e) {
            log.error("Database error while saving log: {}", event, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error processing production order: {}", event, e);
            throw new EventProcessingException(eventName, "Error processing " + eventName, e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.FACTORY_REGISTERED_QUEUE_NAME)
    public void onFactoryRegistered(ProductionFactoryRegisteredMessage event) {
        final String eventName = RabbitMQConfig.FACTORY_REGISTERED_ROUTING_KEY;
        try {
            log.info("Production event received: {}", event);

            String jsonPayload = objectMapper.writeValueAsString(event);

            eventLogService.save(
                    EventType.PRODUCTION_FACTORY_REGISTERED,
                    SourceService.FACTORY,
                    jsonPayload,
                    event.simulationDay(),
                    event.occurredAt());
        } catch (JsonProcessingException e) {
            log.error("Error serializing event to JSON: {}", event, e);
            throw new EventSerializationException(eventName, "Error processing " + eventName, e);
        } catch (DataAccessException e) {
            log.error("Database error while saving log: {}", event, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error processing production order: {}", event, e);
            throw new EventProcessingException(eventName, "Error processing " + eventName, e);
        }
    }
}
