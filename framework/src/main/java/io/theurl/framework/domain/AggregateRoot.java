package io.theurl.framework.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AggregateRoot<ID extends Comparable<ID>> extends Entity<ID> implements IAggregateRoot<ID>, IHasDomainEvents {

    private final List<IDomainEvent> events;

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

    @Override
    public void clearEvents() {
        events.clear();
    }

    @Override
    public <E extends IDomainEvent> void raiseEvent(E event) {
        event.attach(this);
        this.events.add(event);
    }

    @Override
    public <E extends IDomainEvent> void applyEvent(E event) {

    }

    public <E extends IDomainEvent> void applyEvent(Consumer<E> handler, E event) {
        handler.accept(event);
    }

    @Override
    public void attachEvent() {

    }
}
