package io.theurl.identity.domain.event;

import io.theurl.framework.domain.DomainEvent;
import io.theurl.framework.domain.IAggregateRoot;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserUnlockedEvent extends DomainEvent {
    private final Long id;
    private final LocalDateTime unlockTime;

    @Override
    public <ID extends Comparable<ID>> void attach(IAggregateRoot<ID> aggregateRoot) {

    }
}
