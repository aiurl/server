package io.theurl.framework.domain;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class EventAggregate implements IAggregateRoot<String> {

    private final String id;
    private String eventId;
    private LocalDateTime timestamp;
    private String typeName;
    private String eventIntent;
    private String originatorType;
    private String originatorId;
    private Object eventPayload;
    private long eventSequence;

    public EventAggregate(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Object[] getKeys() {
        return new Object[]{id};
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getEventIntent() {
        return eventIntent;
    }

    public void setEventIntent(String eventIntent) {
        this.eventIntent = eventIntent;
    }

    public String getOriginatorType() {
        return originatorType;
    }

    public void setOriginatorType(String originatorType) {
        this.originatorType = originatorType;
    }

    public String getOriginatorId() {
        return originatorId;
    }

    public void setOriginatorId(String originatorId) {
        this.originatorId = originatorId;
    }

    public Object getEventPayload() {
        return eventPayload;
    }

    public void setEventPayload(Object eventPayload) {
        this.eventPayload = eventPayload;
    }

    public long getEventSequence() {
        return eventSequence;
    }

    public void setEventSequence(long eventSequence) {
        this.eventSequence = eventSequence;
    }

}
