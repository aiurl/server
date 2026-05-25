package io.theurl.identity.domain.event;

import io.theurl.framework.domain.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class UserEmailChangedEvent extends DomainEvent {
    private final Long id;
    private final String oldEmail;
    private final String newEmail;
}
