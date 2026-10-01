package org.example.reportsworkshopgft.eventlog.application.exception;

import org.example.reportsworkshopgft.eventlog.domain.EventLogId;

public class EventLogNotFoundException extends RuntimeException {

    private final EventLogId id;

    public EventLogNotFoundException(EventLogId id) {
        super("EventLog not found: " + (id == null ? "null" : id.value()));
        this.id = id;
    }

    public EventLogId getId() {
        return id;
    }
}
