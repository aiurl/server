package io.theurl.bundle.domain.event;

import com.neroyun.mediator.Event;
import io.theurl.framework.domain.DomainEvent;
import lombok.Getter;

@Getter
public class BundleDeletedEvent extends DomainEvent implements Event {
    private final long id;
    private final String vanity;
    private final String name;
    private final Long ownerId;

    public BundleDeletedEvent(long id, String vanity, String name, Long ownerId) {
        this.id = id;
        this.vanity = vanity;
        this.name = name;
        this.ownerId = ownerId;
    }
}
