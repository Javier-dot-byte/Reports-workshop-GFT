package org.example.reportsworkshopgft.eventlog.application.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.example.reportsworkshopgft.eventlog.application.exception.EventLogNotFoundException;
import org.example.reportsworkshopgft.eventlog.application.projections.OrderHistoryProjection;
import org.example.reportsworkshopgft.eventlog.application.projections.SystemStatsProjection;
import org.example.reportsworkshopgft.eventlog.domain.EventLog;
import org.example.reportsworkshopgft.eventlog.domain.EventLogId;
import org.example.reportsworkshopgft.eventlog.domain.EventType;
import org.example.reportsworkshopgft.eventlog.domain.SourceService;
import org.example.reportsworkshopgft.eventlog.infrastructure.EventLogRepositoryJPA;
import org.example.reportsworkshopgft.eventlog.infrastructure.persistence.EventLogIdJPA;
import org.example.reportsworkshopgft.eventlog.infrastructure.persistence.EventLogJPA;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class EventLogServiceImplTest {

    @Mock private EventLogRepositoryJPA jpaRepository;

    @Mock private ObjectMapper objectMapper;

    @InjectMocks private EventLogServiceImpl eventLogService;

    @Test
    void should_find_all_events() {
        EventLogJPA jpaEvent =
                new EventLogJPA(
                        new EventLogIdJPA("1"),
                        EventType.TRUCK_REGISTERED,
                        SourceService.TRANSPORT,
                        "{}",
                        1,
                        "2026-05-11");
        when(jpaRepository.findAll()).thenReturn(List.of(jpaEvent));

        List<EventLog> result = eventLogService.findAllEventsLogs();

        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId().value());
    }

    @Test
    void should_find_all_events_paged() {
        EventLogJPA jpaEvent =
                new EventLogJPA(
                        new EventLogIdJPA("1"),
                        EventType.TRUCK_REGISTERED,
                        SourceService.TRANSPORT,
                        "{}",
                        1,
                        "2026-05-11");
        Pageable pageable = PageRequest.of(0, 10);
        when(jpaRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(jpaEvent), pageable, 1));

        Page<EventLog> result = eventLogService.findAllEventsLogs(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("1", result.getContent().get(0).getId().value());
    }

    @Test
    void should_find_event_by_id() {
        EventLogId domainId = new EventLogId("1");
        EventLogJPA jpaEvent =
                new EventLogJPA(
                        new EventLogIdJPA("1"),
                        EventType.TRUCK_REGISTERED,
                        SourceService.TRANSPORT,
                        "{}",
                        1,
                        "2026-05-11");

        when(jpaRepository.findById(any(EventLogIdJPA.class))).thenReturn(Optional.of(jpaEvent));

        EventLog result = eventLogService.findEventLogById(domainId);

        assertNotNull(result);
        assertEquals("1", result.getId().value());
    }

    @Test
    void should_throw_when_event_is_not_found() {
        EventLogId domainId = new EventLogId("missing-id");
        when(jpaRepository.findById(any(EventLogIdJPA.class))).thenReturn(Optional.empty());

        assertThrows(
                EventLogNotFoundException.class, () -> eventLogService.findEventLogById(domainId));
    }

    @Test
    void should_save_event_object() {
        EventLog domainEvent =
                new EventLog(
                        new EventLogId("1"),
                        EventType.TRUCK_REGISTERED,
                        SourceService.TRANSPORT,
                        "{}",
                        1,
                        "2026-05-11");

        eventLogService.save(domainEvent);

        verify(jpaRepository, times(1)).save(any(EventLogJPA.class));
    }

    @Test
    void should_set_occurred_at_when_event_object_has_null_occurred_at() {
        EventLog domainEvent =
                new EventLog(
                        new EventLogId("1"),
                        EventType.TRUCK_REGISTERED,
                        SourceService.TRANSPORT,
                        "{}",
                        1,
                        null);

        eventLogService.save(domainEvent);

        ArgumentCaptor<EventLogJPA> captor = ArgumentCaptor.forClass(EventLogJPA.class);
        verify(jpaRepository, times(1)).save(captor.capture());

        assertNotNull(captor.getValue().getOccurredAt());
        assertFalse(captor.getValue().getOccurredAt().isBlank());
    }

    @Test
    void should_set_simulation_day_when_event_object_has_null_simulation_day() {
        EventLog domainEvent =
                new EventLog(
                        new EventLogId("1"),
                        EventType.TRUCK_REGISTERED,
                        SourceService.TRANSPORT,
                        "{}",
                        null,
                        "2026-05-11");

        eventLogService.save(domainEvent);

        ArgumentCaptor<EventLogJPA> captor = ArgumentCaptor.forClass(EventLogJPA.class);
        verify(jpaRepository, times(1)).save(captor.capture());

        assertEquals(0, captor.getValue().getSimulationDay());
    }

    @Test
    void should_save_event_from_parameters() {
        eventLogService.save(
                EventType.TRUCK_REGISTERED, SourceService.TRANSPORT, "{}", 1, "2026-05-11");

        ArgumentCaptor<EventLogJPA> captor = ArgumentCaptor.forClass(EventLogJPA.class);
        verify(jpaRepository, times(1)).save(captor.capture());

        assertEquals(EventType.TRUCK_REGISTERED, captor.getValue().getEventType());
    }

    @Test
    void should_set_occurred_at_when_null_is_provided() {
        eventLogService.save(EventType.TRUCK_REGISTERED, SourceService.TRANSPORT, "{}", 1, null);

        ArgumentCaptor<EventLogJPA> captor = ArgumentCaptor.forClass(EventLogJPA.class);
        verify(jpaRepository, times(1)).save(captor.capture());

        assertNotNull(captor.getValue().getOccurredAt());
        assertFalse(captor.getValue().getOccurredAt().isBlank());
    }

    @Test
    void should_set_occurred_at_when_blank_is_provided() {
        eventLogService.save(EventType.TRUCK_REGISTERED, SourceService.TRANSPORT, "{}", 1, " ");

        ArgumentCaptor<EventLogJPA> captor = ArgumentCaptor.forClass(EventLogJPA.class);
        verify(jpaRepository, times(1)).save(captor.capture());

        assertNotNull(captor.getValue().getOccurredAt());
        assertFalse(captor.getValue().getOccurredAt().isBlank());
    }

    @Test
    void should_set_simulation_day_when_null_is_provided() {
        eventLogService.save(
                EventType.TRUCK_REGISTERED, SourceService.TRANSPORT, "{}", null, "2026-05-11");

        ArgumentCaptor<EventLogJPA> captor = ArgumentCaptor.forClass(EventLogJPA.class);
        verify(jpaRepository, times(1)).save(captor.capture());

        assertEquals(0, captor.getValue().getSimulationDay());
    }

    @Test
    void should_calculate_system_stats_correctly() throws Exception {

        EventLogJPA orderCreated = new EventLogJPA();
        orderCreated.setEventType(EventType.PRODUCTION_ORDER_CREATED);
        EventLogJPA orderCompleted = new EventLogJPA();
        orderCompleted.setEventType(EventType.PRODUCTION_ORDER_COMPLETED);
        EventLogJPA orderBlocked = new EventLogJPA();
        orderBlocked.setEventType(EventType.PRODUCTION_ORDER_BLOCKED);

        EventLogJPA validTruck = new EventLogJPA();
        validTruck.setEventType(EventType.TRUCK_STATUS_CHANGED);
        validTruck.setPayload("{\"truckId\":\"T1\", \"status\":\"IN_TRANSIT\"}");

        EventLogJPA corruptTruck = new EventLogJPA();
        corruptTruck.setEventType(EventType.TRUCK_STATUS_CHANGED);
        corruptTruck.setPayload("corrupt");

        when(jpaRepository.findAll())
                .thenReturn(
                        List.of(
                                orderCreated,
                                orderCompleted,
                                orderBlocked,
                                validTruck,
                                corruptTruck));

        JsonNode mockNode = new ObjectMapper().readTree(validTruck.getPayload());
        when(objectMapper.readTree(validTruck.getPayload())).thenReturn(mockNode);
        when(objectMapper.readTree(corruptTruck.getPayload()))
                .thenThrow(new RuntimeException("Parse Error"));

        SystemStatsProjection stats = eventLogService.getSystemStats();

        assertEquals(1, stats.totalOrders());
        assertEquals(1, stats.completedOrders());
        assertEquals(1, stats.blockedOrders());
        assertEquals(1, stats.trucksInTransit());
    }

    @Test
    void should_calculate_order_history_with_updates_and_errors() throws Exception {
        EventLogJPA event1 = new EventLogJPA();
        event1.setEventType(EventType.PRODUCTION_ORDER_CREATED);
        event1.setPayload("{\"orderId\":\"ORD-1\", \"factoryId\":\"FAC-A\"}");
        event1.setSimulationDay(1);

        EventLogJPA event2 = new EventLogJPA();
        event2.setEventType(EventType.PRODUCTION_ORDER_STARTED);
        event2.setPayload("{\"orderId\":\"ORD-1\"}");
        event2.setSimulationDay(2);

        EventLogJPA event3 = new EventLogJPA();
        event3.setEventType(EventType.PRODUCTION_ORDER_COMPLETED);
        event3.setPayload("{\"orderId\":\"ORD-2\"}");
        event3.setSimulationDay(3);

        EventLogJPA event4 = new EventLogJPA();
        event4.setEventType(EventType.PRODUCTION_ORDER_BLOCKED);
        event4.setPayload("{\"orderId\":\"ORD-3\", \"factoryId\":\"FAC-B\"}");
        event4.setSimulationDay(4);

        EventLogJPA corruptEvent = new EventLogJPA();
        corruptEvent.setEventType(EventType.PRODUCTION_ORDER_STARTED);
        corruptEvent.setPayload("corrupt");

        EventLogJPA unrelatedEvent = new EventLogJPA();
        unrelatedEvent.setEventType(EventType.TRUCK_REGISTERED);

        when(jpaRepository.findAll())
                .thenReturn(List.of(event1, event2, event3, event4, corruptEvent, unrelatedEvent));

        ObjectMapper realMapper = new ObjectMapper();
        when(objectMapper.readTree(event1.getPayload()))
                .thenReturn(realMapper.readTree(event1.getPayload()));
        when(objectMapper.readTree(event2.getPayload()))
                .thenReturn(realMapper.readTree(event2.getPayload()));
        when(objectMapper.readTree(event3.getPayload()))
                .thenReturn(realMapper.readTree(event3.getPayload()));
        when(objectMapper.readTree(event4.getPayload()))
                .thenReturn(realMapper.readTree(event4.getPayload()));
        when(objectMapper.readTree(corruptEvent.getPayload()))
                .thenThrow(new RuntimeException("Parse Error"));

        List<OrderHistoryProjection> history = eventLogService.getOrderHistory();

        assertEquals(3, history.size());

        assertEquals("STARTED", history.get(0).status());
        assertEquals("FAC-A", history.get(0).factoryId());

        assertEquals("COMPLETED", history.get(1).status());
        assertEquals("N/A", history.get(1).factoryId());

        assertEquals("BLOCKED", history.get(2).status());
        assertEquals("FAC-B", history.get(2).factoryId());
    }

    @Test
    void should_return_order_history_paged_when_page_has_content() throws Exception {
        EventLogJPA event1 = new EventLogJPA();
        event1.setEventType(EventType.PRODUCTION_ORDER_CREATED);
        event1.setPayload("{\"orderId\":\"ORD-1\", \"factoryId\":\"FAC-A\"}");
        event1.setSimulationDay(1);

        EventLogJPA event2 = new EventLogJPA();
        event2.setEventType(EventType.PRODUCTION_ORDER_STARTED);
        event2.setPayload("{\"orderId\":\"ORD-1\"}");
        event2.setSimulationDay(2);

        EventLogJPA event3 = new EventLogJPA();
        event3.setEventType(EventType.PRODUCTION_ORDER_COMPLETED);
        event3.setPayload("{\"orderId\":\"ORD-2\"}");
        event3.setSimulationDay(3);

        when(jpaRepository.findAll()).thenReturn(List.of(event1, event2, event3));

        ObjectMapper realMapper = new ObjectMapper();
        when(objectMapper.readTree(event1.getPayload()))
                .thenReturn(realMapper.readTree(event1.getPayload()));
        when(objectMapper.readTree(event2.getPayload()))
                .thenReturn(realMapper.readTree(event2.getPayload()));
        when(objectMapper.readTree(event3.getPayload()))
                .thenReturn(realMapper.readTree(event3.getPayload()));

        Page<OrderHistoryProjection> page = eventLogService.getOrderHistory(0, 2);

        assertEquals(2, page.getContent().size());
        assertEquals(2, page.getTotalElements());
        assertEquals("STARTED", page.getContent().get(0).status());
        assertEquals("COMPLETED", page.getContent().get(1).status());
    }

    @Test
    void should_return_empty_order_history_paged_when_page_is_out_of_range() throws Exception {
        EventLogJPA event1 = new EventLogJPA();
        event1.setEventType(EventType.PRODUCTION_ORDER_CREATED);
        event1.setPayload("{\"orderId\":\"ORD-1\", \"factoryId\":\"FAC-A\"}");
        event1.setSimulationDay(1);

        when(jpaRepository.findAll()).thenReturn(List.of(event1));

        ObjectMapper realMapper = new ObjectMapper();
        when(objectMapper.readTree(event1.getPayload()))
                .thenReturn(realMapper.readTree(event1.getPayload()));

        Page<OrderHistoryProjection> page = eventLogService.getOrderHistory(1, 50);

        assertTrue(page.getContent().isEmpty());
        assertEquals(1, page.getTotalElements());
    }

    @Test
    void should_return_unknown_for_unmapped_event_types() {
        assertEquals("UNKNOWN", eventLogService.mapEventToStatus(EventType.TRUCK_REGISTERED));
    }
}
