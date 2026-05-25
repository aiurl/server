package io.theurl.identity.domain.aggregate;

import io.theurl.framework.domain.AggregateRoot;

public class User extends AggregateRoot<Long> {
    /**
     * Initializes the aggregate with the given id.
     *
     * @param id the identifier of the aggregate
     */
    public User(Long id) {
        super(id);
    }
}
