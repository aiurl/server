package io.theurl.framework.domain;

import java.util.List;

/**
 * The HasDomainEvents interface is used to indicate that an entity or aggregate root has domain events.
 * It provides methods for getting the list of domain events, clearing the list of domain events, raising a domain event, applying a domain event, and attaching a domain event.
 * The HasDomainEvents interface is used to implement the Domain Event pattern, which is a way to decouple the domain model from the application layer and to allow for better separation of concerns.
 * The Domain Event pattern is a way to model the domain in a more natural way, by allowing the domain model to raise events that represent changes in the state of the domain model, and by allowing the application layer to handle those events in a more flexible way.
 */
public interface IHasDomainEvents {
    /**
     * Gets the list of domain events that have been raised by the entity or aggregate root.
     * @return the list of domain events
     */
    List<IDomainEvent> getEvents();

    /**
     * Clears the list of domain events that have been raised by the entity or aggregate root.
     */
    void clearEvents();

    /**
     * Raises a domain event that represents a change in the state of the entity or aggregate root.
     * @param event the domain event to be raised
     * @param <E> the type of the domain event
     */
    <E extends IDomainEvent> void raiseEvent(E event);

    /**
     * Applies a domain event to the entity or aggregate root, which means that the state of the entity or aggregate root is changed according to the domain event.
     * @param event the domain event to be applied
     * @param <E> the type of the domain event
     */
    <E extends IDomainEvent> void applyEvent(E event);

    /**
     * Attaches a domain event to the entity or aggregate root, which means that the domain event is added to the list of domain events that have been raised by the entity or aggregate root.
     */
    void attachEvent();
}
