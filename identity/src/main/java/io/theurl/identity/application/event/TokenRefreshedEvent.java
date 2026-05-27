package io.theurl.identity.application.event;

import com.neroyun.mediator.Event;
import io.theurl.framework.domain.ApplicationEvent;
import lombok.Getter;

/**
 * TokenRefreshedEvent is an application event that is published when a JWT token is refreshed.
 * It contains the JTI (JWT ID) of the refreshed token, which can be used to track token refresh events and perform actions such as invalidating old tokens or logging refresh activity.
 */
@Getter
public final class TokenRefreshedEvent extends ApplicationEvent implements Event {
    private final String jti;

    public TokenRefreshedEvent(String jti) {
        this.jti = jti;
    }

}
