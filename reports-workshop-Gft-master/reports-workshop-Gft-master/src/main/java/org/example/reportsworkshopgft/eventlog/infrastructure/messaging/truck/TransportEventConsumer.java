package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.truck;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
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
public class TransportEventConsumer {

    private final EventLogServiceImpl eventLogServiceImpl;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.TRUCK_REGISTERED_QUEUE_NAME)
    public void onTruckRegistered(TruckRegisteredEvent event) {
        final String eventName = "truck.registered.v1";
        try {
            log.info("Transport event received: {}", event);

            String jsonPayload = objectMapper.writeValueAsString(event);

            eventLogServiceImpl.save(
                    EventType.TRUCK_REGISTERED,
                    SourceService.TRANSPORT,
                    jsonPayload,
                    event.timestamp(),
                    LocalDateTime.now().toString());

        } catch (JsonProcessingException e) {
            log.error("Error serializing event to JSON: {}", event, e);
            throw new EventSerializationException(eventName, "Error serializing RabbitMQ event", e);
        } catch (DataAccessException e) {
            log.error("Database error while saving log: {}", event, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error processing transport event: {}", event, e);
            throw new EventProcessingException(eventName, "Error processing RabbitMQ event", e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.TRUCK_POSITION_UPDATE_QUEUE_NAME)
    public void onTruckPositionUpdate(TruckPositionUpdateEvent event) {
        final String eventName = "truck.position.update.v1";
        try {
            log.info("Transport event received: {}", event);

            String jsonPayload = objectMapper.writeValueAsString(event);

            eventLogServiceImpl.save(
                    EventType.TRUCK_POSITION_UPDATED,
                    SourceService.TRANSPORT,
                    jsonPayload,
                    event.simulationDay(),
                    event.timestamp());

        } catch (JsonProcessingException e) {
            log.error("Error serializing event to JSON: {}", event, e);
            throw new EventSerializationException(eventName, "Error serializing RabbitMQ event", e);
        } catch (DataAccessException e) {
            log.error("Database error while saving log: {}", event, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error processing transport event: {}", event, e);
            throw new EventProcessingException(eventName, "Error processing RabbitMQ event", e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.TRUCK_STATUS_CHANGED_QUEUE_NAME)
    public void onTruckStatusChanged(TruckStatusChangedEvent event) {
        final String eventName = "truck.status.changed.v1";
        try {
            log.info("Transport event received: {}", event);

            String jsonPayload = objectMapper.writeValueAsString(event);

            eventLogServiceImpl.save(
                    EventType.TRUCK_STATUS_CHANGED,
                    SourceService.TRANSPORT,
                    jsonPayload,
                    event.simulationDay(),
                    event.timestamp());

        } catch (JsonProcessingException e) {
            log.error("Error serializing event to JSON: {}", event, e);
            throw new EventSerializationException(eventName, "Error serializing RabbitMQ event", e);
        } catch (DataAccessException e) {
            log.error("Database error while saving log: {}", event, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error processing transport event: {}", event, e);
            throw new EventProcessingException(eventName, "Error processing RabbitMQ event", e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.DELIVERY_COMPLETED_QUEUE_NAME)
    public void onDeliveryCompleted(DeliveryCompletedEvent event) {
        final String eventName = "delivery.completed.v1";
        try {
            log.info("Transport event received: {}", event);

            String jsonPayload = objectMapper.writeValueAsString(event);

            eventLogServiceImpl.save(
                    EventType.DELIVERY_COMPLETED,
                    SourceService.TRANSPORT,
                    jsonPayload,
                    event.simulationDay(),
                    event.timestamp());

            eventLogServiceImpl.save(
                    EventType.TRUCK_STATUS_CHANGED,
                    SourceService.TRANSPORT,
                    jsonPayload,
                    event.simulationDay(),
                    event.timestamp());

        } catch (JsonProcessingException e) {
            log.error("Error serializing event to JSON: {}", event, e);
            throw new EventSerializationException(eventName, "Error serializing RabbitMQ event", e);
        } catch (DataAccessException e) {
            log.error("Database error while saving log: {}", event, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error processing transport event: {}", event, e);
            throw new EventProcessingException(eventName, "Error processing RabbitMQ event", e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.TRUCK_DELETED_QUEUE_NAME)
    public void onTruckDeleted(TruckDeletedEvent event) {
        final String eventName = "truck.deleted.v1";
        try {
            log.info("Transport event received: {}", event);
            String jsonPayload = objectMapper.writeValueAsString(event);
            eventLogServiceImpl.save(
                    EventType.TRUCK_DELETED,
                    SourceService.TRANSPORT,
                    jsonPayload,
                    event.simulationDay(),
                    event.timestamp());

        } catch (Exception e) {
            log.error("Error processing truck deleted event: {}", event, e);
            throw new EventProcessingException(eventName, "Error processing RabbitMQ event", e);
        }
    }
}
