package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.exception;

public class EventDeserializationException extends EventConsumerException {

    public EventDeserializationException(String eventName, String message, Throwable cause) {
        super(eventName, message, cause);
    }
}
