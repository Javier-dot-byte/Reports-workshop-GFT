package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.time;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.reportsworkshopgft.eventlog.application.impl.EventLogServiceImpl;
import org.example.reportsworkshopgft.eventlog.domain.EventType;
import org.example.reportsworkshopgft.eventlog.domain.SourceService;
import org.example.reportsworkshopgft.eventlog.infrastructure.messaging.exception.EventProcessingException;
import org.example.reportsworkshopgft.eventlog.infrastructure.messaging.exception.EventSerializationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class TimeEventConsumerTest {

    @Mock private EventLogServiceImpl eventLogServiceImpl;

    @Mock private ObjectMapper objectMapper;

    @InjectMocks private TimeEventConsumer consumer;

    @Test
    void should_process_time_advanced_event() throws Exception {
        TimeAdvancedMessage event = new TimeAdvancedMessage(3, "2024-01-01T00:00:00Z");

        when(objectMapper.writeValueAsString(event)).thenReturn(event.toString());

        consumer.onTimeAdvanced(event);

        verify(eventLogServiceImpl)
                .save(
                        eq(EventType.TIME_ADVANCED),
                        eq(SourceService.TIME),
                        eq(event.toString()),
                        eq(3),
                        eq("2024-01-01T00:00:00Z"));
    }

    @Test
    void should_throw_exception_when_processing_fails() throws Exception {
        TimeAdvancedMessage event = new TimeAdvancedMessage(5, "2024-02-01T10:00:00Z");

        when(objectMapper.writeValueAsString(event)).thenReturn(event.toString());

        doThrow(new RuntimeException("boom"))
                .when(eventLogServiceImpl)
                .save(
                        eq(EventType.TIME_ADVANCED),
                        eq(SourceService.TIME),
                        eq(event.toString()),
                        eq(5),
                        eq("2024-02-01T10:00:00Z"));

        assertThatThrownBy(() -> consumer.onTimeAdvanced(event))
                .isInstanceOf(EventProcessingException.class)
                .hasMessageContaining("Error processing time.advanced.v1");
    }

    @Test
    void should_throw_exception_when_json_processing_fails() throws Exception {
        TimeAdvancedMessage event = new TimeAdvancedMessage(5, "2024-02-01T10:00:00Z");

        when(objectMapper.writeValueAsString(event))
                .thenThrow(new com.fasterxml.jackson.databind.JsonMappingException("JSON Error"));

        assertThatThrownBy(() -> consumer.onTimeAdvanced(event))
                .isInstanceOf(EventSerializationException.class)
                .hasMessage("Error processing time.advanced.v1");
    }

    @Test
    void should_throw_exception_when_data_access_fails() throws Exception {
        TimeAdvancedMessage event = new TimeAdvancedMessage(5, "2024-02-01T10:00:00Z");

        when(objectMapper.writeValueAsString(event)).thenReturn(event.toString());

        doThrow(new DataAccessException("DB Error") {})
                .when(eventLogServiceImpl)
                .save(
                        eq(EventType.TIME_ADVANCED),
                        eq(SourceService.TIME),
                        eq(event.toString()),
                        eq(5),
                        eq("2024-02-01T10:00:00Z"));

        assertThatThrownBy(() -> consumer.onTimeAdvanced(event))
                .isInstanceOf(DataAccessException.class)
                .hasMessage("DB Error");
    }
}
