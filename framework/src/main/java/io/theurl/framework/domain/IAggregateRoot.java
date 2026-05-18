package io.theurl.framework.domain;

/**
 * The AggregateRoot is the root of the domain model.
 * It is responsible for maintaining the consistency of the domain model and for enforcing the business rules.
 * The Aggregate is also responsible for managing the lifecycle of the domain model and for providing a public API for accessing the domain model.
 */
public interface IAggregateRoot<ID extends Comparable<ID>> extends IEntity<ID> {

}
