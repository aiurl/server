package io.theurl.framework.domain;

import io.theurl.framework.core.ObjectId;

import java.util.HashMap;

public abstract class DomainEvent implements IDomainEvent {
    private final String PROPERTY_ID = "io.theurl.framework.domain.event.id";
    private final HashMap<String, Object> properties = new HashMap<>();

    protected DomainEvent() {
        var type = this.getClass().getGenericSuperclass();
        properties.put(PROPERTY_ID, ObjectId.guid());
    }

    public String get(String name) {
        return (String) properties.get(name);
    }


}
