package io.theurl.identity.application.subscriber;

import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.event.UserAuthFailureEvent;
import io.theurl.identity.application.event.UserAuthSuccessEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Scope(BeanScope.PROTOTYPE)
public class LoggingEventSubscriber {

    @Async
    @EventListener
    public void handleUserAuthSuccess(UserAuthSuccessEvent event) {
        ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest().getUserPrincipal();
    }

    @Async
    @EventListener
    public void handleUserAuthFailure(UserAuthFailureEvent event) {

    }
}
