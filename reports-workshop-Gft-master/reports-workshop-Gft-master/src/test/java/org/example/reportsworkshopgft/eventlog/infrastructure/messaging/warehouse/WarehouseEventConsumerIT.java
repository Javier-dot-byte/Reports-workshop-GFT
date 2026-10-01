package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.warehouse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;
import org.example.reportsworkshopgft.AbstractIntegrationTest;
import org.example.reportsworkshopgft.eventlog.infrastructure.EventLogRepositoryJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

class WarehouseEventConsumerIT extends AbstractIntegrationTest {

    @Autowired private RabbitTemplate rabbitTemplate;

    @Autowired private EventLogRepositoryJPA eventLogRepository;

    @BeforeEach
    void cleanDatabase() {
        eventLogRepository.deleteAll();
    }

    @Test
    void should_persist_event_log_when_warehouse_stock_changed_message_is_received() {
        String message =
                """
                {
                  "productId": "abc-123",
                  "quantity": 50,
                  "type": "INCREASE"
                }
                """;

        rabbitTemplate.convertAndSend("warehouse.stock.changed.v1", message);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(
                        () ->
                                assertThat(eventLogRepository.findAll())
                                        .hasSize(1)
                                        .first()
                                        .satisfies(
                                                log -> {
                                                    assertThat(log.getEventType())
                                                            .isEqualTo("WAREHOUSE_STOCK_CHANGED");
                                                    assertThat(log.getSourceService())
                                                            .isEqualTo("WAREHOUSE");
                                                }));
    }

    @Test
    void should_persist_event_log_when_replenishment_requested_message_is_received() {
        String message =
                """
                {
                  "productId": "abc-123",
                  "quantity": 50,
                  "type": "REPLENISHMENT"
                }
                """;

        rabbitTemplate.convertAndSend("replenishment.requested.v1", message);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(
                        () ->
                                assertThat(eventLogRepository.findAll())
                                        .hasSize(1)
                                        .first()
                                        .satisfies(
                                                log -> {
                                                    assertThat(log.getEventType())
                                                            .isEqualTo("REPLENISHMENT_REQUESTED");
                                                    assertThat(log.getSourceService())
                                                            .isEqualTo("WAREHOUSE");
                                                }));
    }

    @Test
    void should_persist_event_log_when_warehouse_order_blocked_message_is_received() {
        String message =
                """
                {
                  "orderId": "123e4567-e89b-12d3-a456-426614174000"
                }
                """;

        rabbitTemplate.convertAndSend("warehouse.order.blocked.v1", message);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(
                        () ->
                                assertThat(eventLogRepository.findAll())
                                        .hasSize(1)
                                        .first()
                                        .satisfies(
                                                log -> {
                                                    assertThat(log.getEventType())
                                                            .isEqualTo("WAREHOUSE_ORDER_BLOCKED");
                                                    assertThat(log.getSourceService())
                                                            .isEqualTo("WAREHOUSE");
                                                }));
    }
}
