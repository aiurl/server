package io.theurl.framework.domain;

import java.time.LocalDateTime;

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
}
