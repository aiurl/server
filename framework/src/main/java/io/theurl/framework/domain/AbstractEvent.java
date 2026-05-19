package io.theurl.framework.domain;

import io.theurl.framework.core.ObjectId;
import io.theurl.framework.reflection.TypeHelper;

import java.time.LocalDateTime;
import java.util.HashMap;

@SuppressWarnings({"unchecked", "unused"})
public abstract class AbstractEvent {
    private final String PROPERTY_ID = "io.theurl.framework.domain.event.id";
    private final String PROPERTY_EVENT_INTENT = "io.theurl.framework.domain.event.intent";
    private final String PROPERTY_ORIGINATOR_TYPE = "io.theurl.framework.domain.event.originator.type";
    private final String PROPERTY_ORIGINATOR_ID = "io.theurl.framework.domain.event.originator.id";
    private final HashMap<String, String> properties = new HashMap<>();
    private long sequence = LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC);

    protected AbstractEvent() {
        var type = this.getClass().getGenericSuperclass();
        setEventIntent(type.getTypeName());
        properties.put(PROPERTY_ID, ObjectId.guid().toString());
    }


    public String get(String name) {
        return properties.get(name);
    }

    public void set(String name, String value) {
        properties.put(name, value);
    }

    <V> V getProperty(String name, Class<V> targetType) {
        var value = get(name);
        if (value == null) {
            return null;
        }
        return (V) TypeHelper.coerceValue(targetType, String.class, value);
    }

    public String getEventId() {
        return properties.get(PROPERTY_ID);
    }

    public void setEventId(String eventId) {
        properties.put(PROPERTY_ID, eventId);
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public String getEventIntent() {
        return get(PROPERTY_EVENT_INTENT);
    }

    public void setEventIntent(String eventIntent) {
        set(PROPERTY_EVENT_INTENT, eventIntent);
    }

    public String getOriginatorType() {
        return get(PROPERTY_ORIGINATOR_TYPE);
    }

    public void setOriginatorType(String originatorType) {
        set(PROPERTY_ORIGINATOR_TYPE, originatorType);
    }

    public String getOriginatorId() {
        return get(PROPERTY_ORIGINATOR_ID);
    }

    public void setOriginatorId(String originatorId) {
        set(PROPERTY_ORIGINATOR_ID, originatorId);
    }
}
