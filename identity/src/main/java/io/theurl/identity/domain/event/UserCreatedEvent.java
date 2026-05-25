package io.theurl.identity.domain.event;

import io.theurl.framework.domain.DomainEvent;
import lombok.Getter;

@Getter
public class UserCreatedEvent extends DomainEvent {
    private final String username;

    public UserCreatedEvent(String username) {
        this.username = username;
    }
}
