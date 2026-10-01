package org.example.reportsworkshopgft.eventlog.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class EventLog {

    private EventLogId id;
    private EventType eventType;
    private SourceService sourceService;
    private String payload;
    private Integer simulationDay;
    private String occurredAt;
}
