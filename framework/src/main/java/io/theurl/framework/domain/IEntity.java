package io.theurl.framework.domain;

public interface IEntity<ID extends Comparable<ID>> {
    ID getId();


    Object[] getKeys();
}
