package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit Tests - EventDeserializationException")
class EventDeserializationExceptionTest {

    @Test
    @DisplayName("Given exception parameters, when initialized, then state must be fully preserved")
    void shouldRetainMessageAndCauseWhenInitialized() {
        String expectedEventName = "ORDER_COMPLETED_EVENT";
        String expectedMessage = "Invalid format structure detected in payload";
        Throwable expectedCause = new IllegalArgumentException("Missing field: orderId");

        EventDeserializationException exception = new EventDeserializationException(
                expectedEventName,
                expectedMessage,
                expectedCause
        );

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
        assertThat(exception.getCause()).isEqualTo(expectedCause);
    }
}