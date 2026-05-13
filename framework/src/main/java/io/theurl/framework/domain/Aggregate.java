package io.theurl.framework.domain;

/**
 * The Aggregate is the root of the domain model.
 * It is responsible for maintaining the consistency of the domain model and for enforcing the business rules.
 * The Aggregate is also responsible for managing the lifecycle of the domain model and for providing a public API for accessing the domain model.
 */
public abstract class Aggregate<ID> {
    private final ID id;

    /**
     * Initializes the aggregate with the given id.
     * @param id the identifier of the aggregate
     */
    protected Aggregate(ID id) {
        this.id = id;
    }

    /**
     * Gets the identifier of the aggregate.
     * @return the identifier of the aggregate
     */
    public ID getId() {
        return id;
    }
}
