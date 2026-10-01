package org.example.reportsworkshopgft.eventlog.infrastructure.messaging.exception;

public class EventProcessingException extends EventConsumerException {

    public EventProcessingException(String eventName, String message, Throwable cause) {
        super(eventName, message, cause);
    }
}
