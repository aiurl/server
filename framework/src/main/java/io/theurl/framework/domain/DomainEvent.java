package io.theurl.framework.domain;

import io.theurl.framework.core.ObjectId;
import io.theurl.framework.reflection.TypeHelper;

import java.time.LocalDateTime;

@SuppressWarnings({"unused", "unchecked"})
public abstract class DomainEvent extends AbstractEvent implements IDomainEvent {

    private Object aggregatePayload;

    public Object getAggregatePayload() {
        return aggregatePayload;
    }

    public void setAggregatePayload(Object aggregatePayload) {
        this.aggregatePayload = aggregatePayload;
    }

    public <ID extends Comparable<ID>> void Attach(IAggregateRoot<ID> aggregate) {
        setOriginatorType(aggregate.getClass().getName());
        setOriginatorId(aggregate.getId().toString());
        setAggregatePayload(aggregate);
    }

    public EventAggregate getEventAggregate() {
        var aggregate = new EventAggregate(ObjectId.guid().toString());
        aggregate.setTypeName(this.getClass().getName());
        aggregate.setEventId(getEventId());
        aggregate.setEventIntent(getEventIntent());
        aggregate.setOriginatorType(getOriginatorType());
        aggregate.setOriginatorId(getOriginatorId());
        aggregate.setTimestamp(LocalDateTime.now());
        aggregate.setEventPayload(this);
        return aggregate;
    }


    public <V> V getAggregate(Class<V> targetType) {
        var aggregate = getAggregatePayload();
        if (aggregate == null) {
            return null;
        }
        if (targetType.isAssignableFrom(aggregate.getClass())) {
            return (V) aggregate;
        }
        return (V) TypeHelper.coerceValue(targetType, aggregate.getClass(), aggregate);
    }
}
