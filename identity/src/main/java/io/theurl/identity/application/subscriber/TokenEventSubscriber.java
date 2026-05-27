package io.theurl.identity.application.subscriber;

import com.neroyun.mediator.Mediator;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.TokenCreateCommand;
import io.theurl.identity.application.command.TokenRevokeCommand;
import io.theurl.identity.application.event.TokenGrantedEvent;
import io.theurl.identity.application.event.TokenRefreshedEvent;
import io.theurl.identity.domain.enums.TokenStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanScope.PROTOTYPE)
public class TokenEventSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenEventSubscriber.class);
    private final Mediator mediator;

    public TokenEventSubscriber(Mediator mediator) {
        this.mediator = mediator;
    }

    @Async
    @EventListener
    public void handleUserAuthSucceedEvent(TokenGrantedEvent event) {
        try {
            var command = new TokenCreateCommand() {{
                setJti(event.getJti());
                setContent(event.getContent());
                setSubject(event.getUserId());
                setExpiresAt(event.getExpiresAt());
                setIssuedAt(event.getIssuedAt());
            }};
            mediator.sendAsync(command)
                    .join();
        } catch (Exception exception) {
            LOGGER.error(exception.getLocalizedMessage(), exception);
        }
    }

    @Async
    @EventListener
    public void handleTokenRefreshedEvent(TokenRefreshedEvent event) {
        try {
            var command = new TokenRevokeCommand(event.getJti(), TokenStatus.REFRESHED);
            mediator.sendAsync(command)
                    .join();
        } catch (Exception exception) {
            LOGGER.error(exception.getLocalizedMessage(), exception);
        }
    }
}
