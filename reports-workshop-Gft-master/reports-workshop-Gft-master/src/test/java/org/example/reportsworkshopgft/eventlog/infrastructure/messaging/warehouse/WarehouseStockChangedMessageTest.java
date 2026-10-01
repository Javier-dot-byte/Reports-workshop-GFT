package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.warehouse;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class WarehouseStockChangedMessageTest {

    @Test
    void should_create_message_with_all_fields() {
        WarehouseStockChangedMessage message =
                new WarehouseStockChangedMessage("abc-123", 50, "INCREASE");

        assertThat(message.productId()).isEqualTo("abc-123");
        assertThat(message.quantity()).isEqualTo(50);
        assertThat(message.type()).isEqualTo("INCREASE");
    }
}
