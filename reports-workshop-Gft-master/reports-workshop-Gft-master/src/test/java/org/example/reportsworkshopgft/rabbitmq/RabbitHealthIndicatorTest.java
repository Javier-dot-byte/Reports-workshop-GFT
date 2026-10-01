package org.example.reportsworkshopgft.rabbitmq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.example.reportsworkshopgft.rabbitmq.config.RabbitHealthIndicator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

@ExtendWith(MockitoExtension.class)
class RabbitHealthIndicatorTest {

    @Mock private ConnectionFactory connectionFactory;

    @Mock private Connection connection;

    @InjectMocks private RabbitHealthIndicator rabbitHealthIndicator;

    @Test
    void health_should_return_UP_when_connection_is_open() {
        when(connectionFactory.createConnection()).thenReturn(connection);
        when(connection.isOpen()).thenReturn(true);
        when(connectionFactory.getHost()).thenReturn("cloudamqp-server");
        when(connectionFactory.getPort()).thenReturn(5672);

        Health health = rabbitHealthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("status", "Connected to CloudAMQP");
        assertThat(health.getDetails()).containsEntry("host", "cloudamqp-server");
        assertThat(health.getDetails()).containsEntry("port", 5672);
        verify(connection).close();
    }

    @Test
    void health_should_return_DOWN_when_connection_is_closed() {
        when(connectionFactory.createConnection()).thenReturn(connection);
        when(connection.isOpen()).thenReturn(false);

        Health health = rabbitHealthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsEntry("error", "Connection is closed");
        verify(connection).close();
    }

    @Test
    void health_should_return_DOWN_when_exception_occurs() {
        when(connectionFactory.createConnection())
                .thenThrow(new RuntimeException("Connection timeout"));

        Health health = rabbitHealthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsEntry("error", "Could not reach CloudAMQP server");
        assertThat(health.getDetails()).containsEntry("message", "Connection timeout");
    }

    @Test
    void health_should_return_DOWN_when_connection_factory_returns_null() {
        when(connectionFactory.createConnection()).thenReturn(null);

        Health health = rabbitHealthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsEntry("error", "Could not reach CloudAMQP server");
        assertThat(health.getDetails()).containsKey("message");
    }

    @Test
    void health_should_return_DOWN_when_close_throws_exception() {
        when(connectionFactory.createConnection()).thenReturn(connection);
        when(connection.isOpen()).thenReturn(true);
        when(connectionFactory.getHost()).thenReturn("cloudamqp-server");
        when(connectionFactory.getPort()).thenReturn(5672);
        doThrow(new RuntimeException("Close failed")).when(connection).close();

        Health health = rabbitHealthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsEntry("error", "Could not reach CloudAMQP server");
        assertThat(health.getDetails()).containsEntry("message", "Close failed");
    }
}
