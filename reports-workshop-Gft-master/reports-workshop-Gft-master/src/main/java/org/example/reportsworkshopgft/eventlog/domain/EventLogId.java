package org.example.reportsworkshopgft.eventlog.domain;

import jakarta.persistence.Embeddable;
import org.example.reportsworkshopgft.eventlog.domain.exception.EventLogIdNotUuidException;
import org.example.reportsworkshopgft.eventlog.domain.exception.InvalidEventLogIdException;

@Embeddable
public record EventLogId(String value) {

    public EventLogId {
        if (value == null || value.isBlank()) {
            throw new InvalidEventLogIdException(value);
        }
    }

    public static EventLogId generate() {
        return new EventLogId(java.util.UUID.randomUUID().toString());
    }

    public java.util.UUID toUUID() {
        try {
            return java.util.UUID.fromString(this.value);
        } catch (IllegalArgumentException e) {
            throw new EventLogIdNotUuidException(this.value, e);
        }
    }

    public String getValue() {
        return this.value;
    }
}
