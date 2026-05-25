package io.theurl.identity.application.subscriber;

import com.neroyun.mediator.Mediator;
import io.theurl.identity.application.command.UserAccessFailureCountCommand;
import io.theurl.identity.application.event.UserAuthFailureEvent;
import io.theurl.identity.application.event.UserAuthSuccessEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@AllArgsConstructor
public class UserAccessFailureCountEventSubscriber {

    private final Mediator mediator;

    @Async
    @EventListener
    public void listen(UserAuthFailureEvent event) {
        if (event.getUserId() == null || event.getUserId() <= 0) {
            return;
        }

        mediator.sendAsync(new UserAccessFailureCountCommand(event.getUserId(), "increase"))
                .join();
    }

    @Async
    @EventListener
    public void listen(UserAuthSuccessEvent event) {
        if (event.getUserId() == null || event.getUserId() <= 0) {
            return;
        }
        mediator.sendAsync(new UserAccessFailureCountCommand(event.getUserId(), "reset"))
                .join();
    }
}
