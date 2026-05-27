package io.theurl.identity.domain.event;

import com.neroyun.mediator.Event;
import io.theurl.framework.domain.DomainEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OnetimePasswordCreatedEvent extends DomainEvent implements Event {
    private String requestId;
    private String recipient;
    private String code;
    private Integer duration;
    private String usage;
}
