package io.theurl.message.domain.aggregate;

import io.theurl.framework.domain.AggregateRoot;

public class Template extends AggregateRoot<Long> {
    /**
     * Initializes the aggregate with the given id.
     *
     * @param id the identifier of the aggregate
     */
    protected Template(Long id) {
        super(id);
    }
}
