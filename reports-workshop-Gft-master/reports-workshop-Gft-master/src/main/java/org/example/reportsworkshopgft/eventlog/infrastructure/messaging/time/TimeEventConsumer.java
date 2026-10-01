package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.time;

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
public class TimeEventConsumer {

    private final EventLogServiceImpl eventLogServiceImpl;
    private final ObjectMapper objectMapper;
    private static final String EVENT_NAME = "time.advanced.v1";

    @RabbitListener(queues = RabbitMQConfig.TIME_ADVANCED_QUEUE_NAME)
    public void onTimeAdvanced(TimeAdvancedMessage event) {
        try {
            log.info("Production event received: {}", event);

            String jsonPayload = objectMapper.writeValueAsString(event);

            eventLogServiceImpl.save(
                    EventType.TIME_ADVANCED,
                    SourceService.TIME,
                    jsonPayload,
                    event.simulationDay(),
                    String.valueOf(event.occurredAt()));
        } catch (JsonProcessingException e) {
            log.error("Error serializing event to JSON: {}", event, e);
            throw new EventSerializationException(
                    EVENT_NAME, "Error processing time.advanced.v1", e);
        } catch (DataAccessException e) {
            log.error("Database error while saving log: {}", event, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected error processing production order: {}", event, e);
            throw new EventProcessingException(EVENT_NAME, "Error processing time.advanced.v1", e);
        }
    }
}
