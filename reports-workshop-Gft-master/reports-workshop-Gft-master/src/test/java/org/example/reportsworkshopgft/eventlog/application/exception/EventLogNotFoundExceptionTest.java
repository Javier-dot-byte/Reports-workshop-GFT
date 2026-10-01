package org.example.reportsworkshopgft.eventlog.application.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.reportsworkshopgft.eventlog.domain.EventLogId;
import org.junit.jupiter.api.Test;

class EventLogNotFoundExceptionTest {

    @Test
    void should_expose_id_and_message_when_id_is_present() {
        EventLogId id = new EventLogId("abc");
        EventLogNotFoundException ex = new EventLogNotFoundException(id);

        assertThat(ex.getId()).isEqualTo(id);
        assertThat(ex.getMessage()).contains("abc");
    }

    @Test
    void should_format_message_when_id_is_null() {
        EventLogNotFoundException ex = new EventLogNotFoundException(null);

        assertThat(ex.getId()).isNull();
        assertThat(ex.getMessage()).contains("null");
    }
}
