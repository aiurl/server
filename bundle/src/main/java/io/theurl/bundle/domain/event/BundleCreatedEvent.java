package io.theurl.bundle.domain.event;

import com.neroyun.mediator.Event;
import io.theurl.framework.domain.DomainEvent;
import lombok.Getter;

@Getter
public class BundleCreatedEvent extends DomainEvent implements Event {
    private final String type;
    private final String vanity;
    private final String name;

    public BundleCreatedEvent(String type, String vanity, String name) {
        this.type = type;
        this.vanity = vanity;
        this.name = name;
    }
}
