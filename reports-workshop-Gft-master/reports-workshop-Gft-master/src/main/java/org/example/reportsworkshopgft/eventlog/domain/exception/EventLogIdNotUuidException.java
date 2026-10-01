package org.example.reportsworkshopgft.eventlog.domain.exception;

public class EventLogIdNotUuidException extends IllegalArgumentException {

    public EventLogIdNotUuidException(String value, Throwable cause) {
        super("EventLogId is not a UUID: " + value, cause);
    }
}
