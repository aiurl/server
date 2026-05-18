package io.theurl.framework.domain;

/**
 * Represents a domain event in the system, which is an occurrence that has significance to the business domain. A domain event is typically used to capture changes in the state of an aggregate root and to trigger side effects or reactions in other parts of the system.
 * The DomainEvent interface defines the contract for attaching an aggregate root to the event and retrieving the associated event aggregate. Implementations of this interface can be used to represent specific events that occur within the domain, allowing for a clear separation of concerns and enabling event-driven architecture patterns.
 */
public interface IDomainEvent {
    /**
     * Gets the unique identifier of the domain event, which can be used to track and correlate events across the system.
     *
     * @return the unique identifier of the domain event
     */
    String getEventId();

    /**
     * Gets the sequence number of the domain event, which can be used to order events and ensure consistency when processing them. The sequence number is typically assigned by the event store or event bus when the event is raised.
     *
     * @return the sequence number of the domain event
     */
    long getSequence();

    /**
     * Sets the sequence number of the domain event, which can be used to order events and ensure consistency when processing them. The sequence number is typically assigned by the event store or event bus when the event is raised.
     *
     * @param sequence the sequence number of the domain event
     */
    void setSequence(long sequence);

    String getEventIntent();

    void setEventIntent(String eventIntent);

    String getOriginatorType();

    void setOriginatorType(String originatorType);

    String getOriginatorId();

    void setOriginatorId(String originatorId);

    /**
     * Attaches the given aggregate root to this domain event, allowing the event to be associated with the aggregate's state and behavior.
     *
     * @param aggregateRoot the aggregate root to attach
     * @param <ID>          the type of the aggregate root's identifier
     */
    <ID extends Comparable<ID>> void attach(IAggregateRoot<ID> aggregateRoot);

    /**
     * Gets the event aggregate associated with this domain event.
     *
     * @return the event aggregate
     */
    EventAggregate getEventAggregate();
}
