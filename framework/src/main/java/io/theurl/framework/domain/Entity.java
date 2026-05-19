package io.theurl.framework.domain;

public abstract class Entity<ID extends Comparable<ID>> implements IEntity<ID> {

    private final ID id;

    protected Entity(ID id) {
        this.id = id;
    }

    @Override
    public ID getId() {
        return id;
    }

    @Override
    public Object[] getKeys() {
        return new Object[]{id};
    }
}
