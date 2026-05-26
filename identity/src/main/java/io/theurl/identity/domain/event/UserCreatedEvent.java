package io.theurl.identity.domain.event;

import io.theurl.framework.domain.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class UserCreatedEvent extends DomainEvent {
    private final String username;
    private final String nickname;
    private final String email;
    private final String phone;
}
