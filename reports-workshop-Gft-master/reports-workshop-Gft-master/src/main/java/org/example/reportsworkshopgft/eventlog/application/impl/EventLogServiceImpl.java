package org.example.reportsworkshopgft.eventlog.application.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import org.example.reportsworkshopgft.eventlog.application.EventLogService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventLogServiceImpl implements EventLogService {

    private final EventLogRepositoryJPA jpaRepository;
    private final ObjectMapper objectMapper;

    public EventLogServiceImpl(EventLogRepositoryJPA jpaRepository, ObjectMapper objectMapper) {
        this.jpaRepository = jpaRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<EventLog> findAllEventsLogs() {
        return jpaRepository.findAll().stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    public Page<EventLog> findAllEventsLogs(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(this::mapToDomain);
    }

    @Override
    public EventLog findEventLogById(EventLogId id) {
        EventLogIdJPA jpaId = new EventLogIdJPA(id.value());
        return jpaRepository
                .findById(jpaId)
                .map(this::mapToDomain)
                .orElseThrow(() -> new EventLogNotFoundException(id));
    }

    @Override
    @Transactional
    public void save(EventLog eventLog) {
        EventLogJPA jpaEntity = mapToJPA(eventLog);
        jpaRepository.save(jpaEntity);
    }

    @Override
    public List<OrderHistoryProjection> getOrderHistory() {
        List<EventLogJPA> allEvents = jpaRepository.findAll();
        return buildOrderHistoryMap(allEvents);
    }

    public Page<OrderHistoryProjection> getOrderHistory(int page, int size) {
        List<EventLogJPA> allEvents = jpaRepository.findAll();
        List<OrderHistoryProjection> allHistory = buildOrderHistoryMap(allEvents);
        int start = page * size;
        int end = Math.min(start + size, allHistory.size());
        List<OrderHistoryProjection> pageContent =
                start >= allHistory.size() ? List.of() : allHistory.subList(start, end);
        return new org.springframework.data.domain.PageImpl<>(
                pageContent,
                org.springframework.data.domain.PageRequest.of(page, size),
                allHistory.size());
    }

    private List<OrderHistoryProjection> buildOrderHistoryMap(List<EventLogJPA> allEvents) {

        Map<String, OrderHistoryProjection> historyMap = new LinkedHashMap<>();

        for (EventLogJPA log : allEvents) {
            if (isProductionOrderEvent(log.getEventType())) {
                try {
                    JsonNode payload = objectMapper.readTree(log.getPayload());
                    String orderId = payload.get("orderId").asText();

                    String factoryId =
                            payload.has("factoryId")
                                    ? payload.get("factoryId").asText()
                                    : (historyMap.containsKey(orderId)
                                            ? historyMap.get(orderId).factoryId()
                                            : "N/A");

                    String status = mapEventToStatus(log.getEventType());

                    historyMap.put(
                            orderId,
                            new OrderHistoryProjection(
                                    orderId, factoryId, status, log.getSimulationDay()));
                } catch (Exception e) {
                    System.err.println("Error parsing order event: " + e.getMessage());
                }
            }
        }
        return new ArrayList<>(historyMap.values());
    }

    private boolean isProductionOrderEvent(EventType type) {
        return type == EventType.PRODUCTION_ORDER_CREATED
                || type == EventType.PRODUCTION_ORDER_STARTED
                || type == EventType.PRODUCTION_ORDER_COMPLETED
                || type == EventType.PRODUCTION_ORDER_BLOCKED;
    }

    String mapEventToStatus(EventType type) {
        return switch (type) {
            case PRODUCTION_ORDER_CREATED -> "CREATED";
            case PRODUCTION_ORDER_STARTED -> "STARTED";
            case PRODUCTION_ORDER_COMPLETED -> "COMPLETED";
            case PRODUCTION_ORDER_BLOCKED -> "BLOCKED";
            default -> "UNKNOWN";
        };
    }

    @Override
    public SystemStatsProjection getSystemStats() {

        List<EventLogJPA> allEvents = jpaRepository.findAll();
        ;

        int totalOrders = 0;
        int completedOrders = 0;
        int blockedOrders = 0;

        Map<String, String> truckStatuses = new HashMap<>();

        for (EventLogJPA log : allEvents) {
            if (log.getEventType() == EventType.PRODUCTION_ORDER_CREATED) totalOrders++;
            if (log.getEventType() == EventType.PRODUCTION_ORDER_COMPLETED) completedOrders++;
            if (log.getEventType() == EventType.PRODUCTION_ORDER_BLOCKED) blockedOrders++;

            if (log.getEventType() == EventType.TRUCK_STATUS_CHANGED) {
                try {

                    JsonNode payload = objectMapper.readTree(log.getPayload());
                    String truckId = payload.get("truckId").asText();
                    String status = payload.get("status").asText();
                    truckStatuses.put(truckId, status);
                } catch (Exception e) {

                    System.err.println("Error parsing truck status event: " + e.getMessage());
                }
            }
        }

        int trucksInTransit =
                (int)
                        truckStatuses.values().stream()
                                .filter(status -> status.equals("IN_TRANSIT"))
                                .count();

        return new SystemStatsProjection(
                totalOrders, completedOrders, blockedOrders, trucksInTransit);
    }

    @Transactional
    public void save(
            EventType eventType,
            SourceService sourceService,
            String payload,
            Integer simulationDay,
            String occurredAt) {
        save(
                EventLog.builder()
                        .id(EventLogId.generate())
                        .eventType(eventType)
                        .sourceService(sourceService)
                        .payload(payload)
                        .simulationDay(simulationDay == null ? 0 : simulationDay)
                        .occurredAt(normalizeOccurredAt(occurredAt))
                        .build());
    }

    private EventLog mapToDomain(EventLogJPA jpaEntity) {
        return new EventLog(
                new EventLogId(jpaEntity.getId().getValue()),
                jpaEntity.getEventType(),
                jpaEntity.getSourceService(),
                jpaEntity.getPayload(),
                jpaEntity.getSimulationDay(),
                jpaEntity.getOccurredAt());
    }

    private EventLogJPA mapToJPA(EventLog domain) {
        EventLogIdJPA jpaId = new EventLogIdJPA(domain.getId().value());
        return new EventLogJPA(
                jpaId,
                domain.getEventType(),
                domain.getSourceService(),
                domain.getPayload(),
                domain.getSimulationDay() == null ? 0 : domain.getSimulationDay(),
                normalizeOccurredAt(domain.getOccurredAt()));
    }

    private String normalizeOccurredAt(String occurredAt) {
        if (occurredAt == null || occurredAt.isBlank()) {
            return Instant.now().toString();
        }
        return occurredAt;
    }
}
