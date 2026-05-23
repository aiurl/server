package io.theurl.identity.application.event;

import com.neroyun.mediator.Event;
import io.theurl.framework.domain.ApplicationEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAuthFailureEvent extends ApplicationEvent implements Event {
    private String userId;
    private String username;
    private String grantType;
    private LocalDateTime grantTime;
    private Map<String, String> data;
    private String error;
}
