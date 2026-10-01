package org.example.reportsworkshopgft.eventlog.infrastructure.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.reportsworkshopgft.eventlog.domain.EventType;
import org.example.reportsworkshopgft.eventlog.domain.SourceService;

@Entity
@Table(name = "event_log")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventLogJPA {

    @EmbeddedId
    @Column(name = "id", nullable = false)
    private EventLogIdJPA id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 100)
    private EventType eventType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "source_service", nullable = false, length = 100)
    private SourceService sourceService;

    @NotNull
    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;

    @NotNull
    @Column(name = "simulation_day", nullable = false)
    private Integer simulationDay;

    @Size(max = 50)
    @NotNull
    @Column(name = "occurred_at", nullable = false, length = 50)
    private String occurredAt;
}
