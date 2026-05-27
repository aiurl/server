package io.theurl.identity.application.subscriber;

import com.neroyun.mediator.Mediator;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.UserAccessFailureCountCommand;
import io.theurl.identity.application.event.UserAuthFailureEvent;
import io.theurl.identity.application.event.UserAuthSuccessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanScope.PROTOTYPE)
public class UserEventSubscriber {
    private final Logger LOGGER = LoggerFactory.getLogger(UserEventSubscriber.class);
    private final Mediator mediator;

    public UserEventSubscriber(Mediator mediator) {
        this.mediator = mediator;
    }

    @Async
    @EventListener
    public void handleUserAuthFailureEvent(UserAuthFailureEvent event) {
        try {
            if (event.getUserId() == null || event.getUserId() <= 0) {
                return;
            }

            mediator.sendAsync(new UserAccessFailureCountCommand(event.getUserId(), "increase"))
                    .join();
        } catch (Exception exception) {
            LOGGER.error(exception.getLocalizedMessage(), exception);
        }
    }

    @Async
    @EventListener
    public void handleUserAuthSuccessEvent(UserAuthSuccessEvent event) {
        try {
            if (event.getUserId() == null || event.getUserId() <= 0) {
                return;
            }
            mediator.sendAsync(new UserAccessFailureCountCommand(event.getUserId(), "reset"))
                    .join();
        } catch (Exception exception) {
            LOGGER.error(exception.getLocalizedMessage(), exception);
        }
    }
}
