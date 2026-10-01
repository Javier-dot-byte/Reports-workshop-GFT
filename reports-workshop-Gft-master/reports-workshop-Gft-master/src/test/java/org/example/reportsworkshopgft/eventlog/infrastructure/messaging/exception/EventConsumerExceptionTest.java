package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EventConsumerExceptionTest {

    @Test
    void should_expose_event_name() {
        EventProcessingException ex =
                new EventProcessingException(
                        "time.advanced.v1", "boom", new RuntimeException("cause"));

        assertThat(ex.getEventName()).isEqualTo("time.advanced.v1");
    }
}
