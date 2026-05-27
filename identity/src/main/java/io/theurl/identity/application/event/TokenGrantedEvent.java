package io.theurl.identity.application.event;

import com.neroyun.mediator.Event;
import io.theurl.framework.domain.ApplicationEvent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TokenGrantedEvent extends ApplicationEvent implements Event {

    private final String jti;
    private final Long userId;
    private final String content;
    private LocalDateTime expiresAt;
    private LocalDateTime issuedAt;

    public TokenGrantedEvent(String jti, Long userId, String content) {
        this.jti = jti;
        this.userId = userId;
        this.content = content;
    }


}
