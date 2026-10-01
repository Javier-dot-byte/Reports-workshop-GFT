package org.example.reportsworkshopgft.eventlog.application;

import java.util.List;
import org.example.reportsworkshopgft.eventlog.application.projections.OrderHistoryProjection;
import org.example.reportsworkshopgft.eventlog.application.projections.SystemStatsProjection;
import org.example.reportsworkshopgft.eventlog.domain.EventLog;
import org.example.reportsworkshopgft.eventlog.domain.EventLogId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventLogService {
    List<EventLog> findAllEventsLogs();

    EventLog findEventLogById(EventLogId id);

    void save(EventLog eventLog);

    List<OrderHistoryProjection> getOrderHistory();

    Page<EventLog> findAllEventsLogs(Pageable pageable);

    Page<OrderHistoryProjection> getOrderHistory(int page, int size);

    SystemStatsProjection getSystemStats();
}
