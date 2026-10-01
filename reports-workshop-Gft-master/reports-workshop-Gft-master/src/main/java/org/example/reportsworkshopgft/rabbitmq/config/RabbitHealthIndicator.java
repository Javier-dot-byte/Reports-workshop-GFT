package org.example.reportsworkshopgft.rabbitmq.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitHealthIndicator implements HealthIndicator {

    private final ConnectionFactory connectionFactory;

    @Override
    public Health health() {
        try (Connection connection = connectionFactory.createConnection()) {
            if (connection.isOpen()) {
                return Health.up()
                        .withDetail("status", "Connected to CloudAMQP")
                        .withDetail("host", connectionFactory.getHost())
                        .withDetail("port", connectionFactory.getPort())
                        .build();
            } else {
                return Health.down().withDetail("error", "Connection is closed").build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", "Could not reach CloudAMQP server")
                    .withDetail("message", e.getMessage())
                    .build();
        }
    }
}
