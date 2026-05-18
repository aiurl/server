package io.theurl.framework.core;

import io.theurl.framework.utility.SnowflakeId;

import java.util.UUID;

public final class ObjectId {
    private final Object value;

    public Object getValue() {
        return value;
    }

    public ObjectId(long value) {
        this.value = value;
    }

    public ObjectId(String value) {
        this.value = value;
    }

    public ObjectId(UUID value) {
        this.value = value;
    }

    public ObjectId(Integer value) {
        this.value = value;
    }

    public static ObjectId snowflake() {
        return new ObjectId(SnowflakeId.getInstance().nextId());
    }

    public static ObjectId guid() {
        return new ObjectId(UUID.randomUUID());
    }

    public static ObjectId random() {
        return new ObjectId(UUID.randomUUID());
    }

    public static ObjectId ulid() {
        return new ObjectId(UUID.randomUUID());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ObjectId objectId = (ObjectId) obj;
        return value.equals(objectId.value);
    }
}
