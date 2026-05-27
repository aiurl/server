package io.theurl.framework.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AggregateRoot<ID extends Comparable<ID>> extends Entity<ID> implements IAggregateRoot<ID>, IHasDomainEvents {

    private final List<IDomainEvent> events;

    private final Map<Class<? extends IDomainEvent>, Consumer<IDomainEvent>> eventHandlers = new HashMap<>();

    /**
     * Initializes the aggregate with the given id.
     *
     * @param id the identifier of the aggregate
     */
    protected AggregateRoot(ID id) {
        super(id);
        this.events = new ArrayList<>();
    }

    @Override
    public List<IDomainEvent> getEvents() {
        return List.copyOf(events);
    }

    public boolean hasEvents() {
        return events != null && !events.isEmpty();
    }

    @Override
    public void clearEvents() {
        events.clear();
    }

    @Override
    public <E extends IDomainEvent> void raiseEvent(E event) {
        applyEvent(event);
        this.events.add(event);
    }

    @Override
    public <E extends IDomainEvent> void applyEvent(E event) {
        var handler = eventHandlers.get(event.getClass());
        if (handler != null) {
            handler.accept(event);
        }
    }

    public <E extends IDomainEvent> void registerEvent(Class<E> eventType, Consumer<E> handler) {
        eventHandlers.put(eventType, event -> handler.accept(eventType.cast(event)));
    }

    @Override
    public void attachEvent() {
        for (var event : events) {
            event.attach(this);
        }
    }
}
