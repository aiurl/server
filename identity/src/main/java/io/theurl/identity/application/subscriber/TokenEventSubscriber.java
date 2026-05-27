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

import java.time.LocalDateTime;

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
            setJti((String) event.getData().get("jti"));
            setContent((String) event.getData().get("jwt"));
            setSubject(event.getUserId());
            setIssuedAt((LocalDateTime) event.getData().get("iat"));
            setExpiresAt((LocalDateTime) event.getData().get("exp"));
        }};

        mediator.sendAsync(command)
                .join();
    }
}
