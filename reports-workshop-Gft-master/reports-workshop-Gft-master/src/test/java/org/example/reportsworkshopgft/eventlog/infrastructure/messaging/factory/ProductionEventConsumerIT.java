package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.factory;

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

class ProductionEventConsumerIT extends AbstractIntegrationTest {

    @Autowired private RabbitTemplate rabbitTemplate;

    @Autowired private EventLogRepositoryJPA eventLogRepository;

    @BeforeEach
    void cleanDatabase() {
        eventLogRepository.deleteAll();
    }

    @Test
    void should_persist_event_log_when_production_order_created_is_received() {
        ProductionOrderCreatedMessage message =
                new ProductionOrderCreatedMessage(
                        "order-1", "factory-1", "product-1", 3, 5, "2025-01-01T00:00:00");

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCTION_EXCHANGE,
                RabbitMQConfig.PRODUCTION_ORDER_CREATED_ROUTING_KEY,
                message);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(
                        () ->
                                assertThat(eventLogRepository.findAll())
                                        .hasSize(1)
                                        .first()
                                        .satisfies(
                                                log -> {
                                                    assertThat(log.getEventType())
                                                            .isEqualTo("PRODUCTION_ORDER_CREATED");
                                                    assertThat(log.getSourceService())
                                                            .isEqualTo("FACTORY");
                                                    assertThat(log.getSimulationDay()).isEqualTo(5);
                                                }));
    }

    @Test
    void should_persist_event_log_when_production_order_started_is_received() {
        ProductionOrderStartedMessage message =
                new ProductionOrderStartedMessage("order-1", "factory-1", 2, "2025-01-01T00:00:00");

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCTION_EXCHANGE,
                RabbitMQConfig.PRODUCTION_ORDER_STARTED_ROUTING_KEY,
                message);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(
                        () ->
                                assertThat(eventLogRepository.findAll())
                                        .hasSize(1)
                                        .first()
                                        .satisfies(
                                                log -> {
                                                    assertThat(log.getEventType())
                                                            .isEqualTo("PRODUCTION_ORDER_STARTED");
                                                    assertThat(log.getSourceService())
                                                            .isEqualTo("FACTORY");
                                                    assertThat(log.getSimulationDay()).isEqualTo(2);
                                                }));
    }

    @Test
    void should_persist_event_log_when_production_order_completed_is_received() {
        ProductionOrderCompletedMessage message =
                new ProductionOrderCompletedMessage(
                        "order-1", "factory-1", 7, "2025-01-01T00:00:00");

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCTION_EXCHANGE,
                RabbitMQConfig.PRODUCTION_ORDER_COMPLETED_ROUTING_KEY,
                message);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(
                        () ->
                                assertThat(eventLogRepository.findAll())
                                        .hasSize(1)
                                        .first()
                                        .satisfies(
                                                log -> {
                                                    assertThat(log.getEventType())
                                                            .isEqualTo(
                                                                    "PRODUCTION_ORDER_COMPLETED");
                                                    assertThat(log.getSourceService())
                                                            .isEqualTo("FACTORY");
                                                    assertThat(log.getSimulationDay()).isEqualTo(7);
                                                }));
    }

    @Test
    void should_persist_event_log_when_production_order_blocked_is_received() {
        ProductionOrderBlockedMessage message =
                new ProductionOrderBlockedMessage(4, "2025-01-01T00:00:00");

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCTION_EXCHANGE,
                RabbitMQConfig.PRODUCTION_ORDER_BLOCKED_ROUTING_KEY,
                message);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(
                        () ->
                                assertThat(eventLogRepository.findAll())
                                        .hasSize(1)
                                        .first()
                                        .satisfies(
                                                log -> {
                                                    assertThat(log.getEventType())
                                                            .isEqualTo("PRODUCTION_ORDER_BLOCKED");
                                                    assertThat(log.getSourceService())
                                                            .isEqualTo("FACTORY");
                                                    assertThat(log.getSimulationDay()).isEqualTo(4);
                                                }));
    }

    @Test
    void should_persist_event_log_when_recipe_registered_is_received() {
        ProductionRecipeRegisteredMessage message =
                new ProductionRecipeRegisteredMessage(1, "2025-01-01T00:00:00");

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCTION_EXCHANGE,
                RabbitMQConfig.RECIPE_REGISTERED_ROUTING_KEY,
                message);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(
                        () ->
                                assertThat(eventLogRepository.findAll())
                                        .hasSize(1)
                                        .first()
                                        .satisfies(
                                                log -> {
                                                    assertThat(log.getEventType())
                                                            .isEqualTo(
                                                                    "PRODUCTION_RECIPE_REGISTERED");
                                                    assertThat(log.getSourceService())
                                                            .isEqualTo("FACTORY");
                                                    assertThat(log.getSimulationDay()).isEqualTo(1);
                                                }));
    }

    @Test
    void should_persist_event_log_when_factory_registered_is_received() {
        ProductionFactoryRegisteredMessage message =
                new ProductionFactoryRegisteredMessage(1, "2025-01-01T00:00:00");

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCTION_EXCHANGE,
                RabbitMQConfig.FACTORY_REGISTERED_ROUTING_KEY,
                message);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(
                        () ->
                                assertThat(eventLogRepository.findAll())
                                        .hasSize(1)
                                        .first()
                                        .satisfies(
                                                log -> {
                                                    assertThat(log.getEventType())
                                                            .isEqualTo(
                                                                    "PRODUCTION_FACTORY_REGISTERED");
                                                    assertThat(log.getSourceService())
                                                            .isEqualTo("FACTORY");
                                                    assertThat(log.getSimulationDay()).isEqualTo(1);
                                                }));
    }
}
