package io.theurl.identity.application.subscriber;

import com.neroyun.mediator.Mediator;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.TokenCreateCommand;
import io.theurl.identity.application.event.UserAuthSuccessEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TokenEventSubscriber {
    private final Mediator mediator;

    public TokenEventSubscriber(Mediator mediator) {
        this.mediator = mediator;
    }

    @Async
    @EventListener
    public void handleUserAuthSucceedEvent(UserAuthSuccessEvent event) {
        var command = new TokenCreateCommand() {{
            setJti(event.getData().get("jti"));
            setContent(event.getData().get("jwt"));
            setSubject(event.getUserId());
        }};

        mediator.sendAsync(command)
                .join();
    }
}
