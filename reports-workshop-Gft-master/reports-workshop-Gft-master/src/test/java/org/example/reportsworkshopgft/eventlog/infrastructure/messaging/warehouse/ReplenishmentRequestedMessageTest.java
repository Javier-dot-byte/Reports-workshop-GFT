package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.warehouse;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReplenishmentRequestedMessageTest {

    @Test
    void should_create_message_with_all_fields() {
        ReplenishmentRequestedMessage message =
                new ReplenishmentRequestedMessage("abc-123", 50, "REPLENISHMENT");

        assertThat(message.productId()).isEqualTo("abc-123");
        assertThat(message.quantity()).isEqualTo(50);
        assertThat(message.type()).isEqualTo("REPLENISHMENT");
    }

    @Test
    void should_convert_to_payload_string() {
        ReplenishmentRequestedMessage message =
                new ReplenishmentRequestedMessage("abc-123", 50, "REPLENISHMENT");

        assertThat(message.toPayload()).contains("productId");
        assertThat(message.toPayload()).contains("abc-123");
        assertThat(message.toPayload()).contains("REPLENISHMENT");
    }
}
