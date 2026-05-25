package io.theurl.identity.domain.event;

import io.theurl.framework.domain.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public final class UserPasswordChangedEvent extends DomainEvent {
    private final Long id;
    private final LocalDateTime changeTime;
    private final String changeType;
}
