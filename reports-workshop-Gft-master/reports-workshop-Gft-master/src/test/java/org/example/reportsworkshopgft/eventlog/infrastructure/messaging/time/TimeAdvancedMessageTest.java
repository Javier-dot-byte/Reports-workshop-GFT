package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.time;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TimeAdvancedMessageTest {

    @Test
    void should_create_message_with_all_fields() {
        TimeAdvancedMessage message = new TimeAdvancedMessage(3, "2024-01-01T00:00:00Z");

        assertThat(message.simulationDay()).isEqualTo(3);
        assertThat(message.occurredAt()).isEqualTo("2024-01-01T00:00:00Z");
    }
}
