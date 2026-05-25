package io.theurl.identity.domain.event;

import io.theurl.framework.domain.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class UserPhoneChangedEvent extends DomainEvent {
    private final Long id;
    private final String oldPhone;
    private final String newPhone;
}
