package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.exception;

public class EventSerializationException extends EventConsumerException {

    public EventSerializationException(String eventName, String message, Throwable cause) {
        super(eventName, message, cause);
    }
}
