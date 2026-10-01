package org.example.reportsworkshopgft.shared.infrastructure.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.example.reportsworkshopgft.eventlog.application.exception.EventLogNotFoundException;
import org.example.reportsworkshopgft.eventlog.domain.EventLogId;
import org.example.reportsworkshopgft.eventlog.domain.exception.EventLogIdNotUuidException;
import org.example.reportsworkshopgft.eventlog.domain.exception.InvalidEventLogIdException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    void should_map_not_found_to_404_with_body() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/reports/123");

        var response =
                handler.handleNotFound(
                        new EventLogNotFoundException(new EventLogId("123")), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().path()).isEqualTo("/reports/123");
    }

    @Test
    void should_map_bad_request_to_400_for_invalid_id() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/reports/ ");

        var response = handler.handleBadRequest(new InvalidEventLogIdException(" "), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
    }

    @Test
    void should_map_bad_request_to_400_for_non_uuid_id() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/reports/not-a-uuid");

        var response =
                handler.handleBadRequest(
                        new EventLogIdNotUuidException(
                                "not-a-uuid", new IllegalArgumentException("bad")),
                        request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().path()).isEqualTo("/reports/not-a-uuid");
    }
}
