package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.time;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;
import org.example.reportsworkshopgft.AbstractIntegrationTest;
import org.example.reportsworkshopgft.eventlog.infrastructure.EventLogRepositoryJPA;
import org.example.reportsworkshopgft.rabbitmq.RabbitMQConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

class TimeEventConsumerIT extends AbstractIntegrationTest {

    @Autowired private RabbitTemplate rabbitTemplate;

    @Autowired private EventLogRepositoryJPA eventLogRepository;

    @BeforeEach
    void cleanDatabase() {
        eventLogRepository.deleteAll();
    }

    @Test
    void should_persist_event_log_when_time_advanced_message_is_received() {
        TimeAdvancedMessage message = new TimeAdvancedMessage(3, "2025-01-01T00:00:00");

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TIME_EXCHANGE, RabbitMQConfig.TIME_ADVANCED_ROUTING_KEY, message);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(
                        () ->
                                assertThat(eventLogRepository.findAll())
                                        .hasSize(1)
                                        .first()
                                        .satisfies(
                                                log -> {
                                                    assertThat(log.getEventType())
                                                            .isEqualTo("TIME_ADVANCED");
                                                    assertThat(log.getSourceService())
                                                            .isEqualTo("TIME");
                                                    assertThat(log.getSimulationDay()).isEqualTo(3);
                                                }));
    }
}
