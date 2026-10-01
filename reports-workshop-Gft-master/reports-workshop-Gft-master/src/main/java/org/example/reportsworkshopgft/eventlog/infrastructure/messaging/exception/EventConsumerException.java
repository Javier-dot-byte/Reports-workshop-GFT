package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.exception;

public abstract class EventConsumerException extends RuntimeException {

    private final String eventName;

    protected EventConsumerException(String eventName, String message, Throwable cause) {
        super(message, cause);
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
}
