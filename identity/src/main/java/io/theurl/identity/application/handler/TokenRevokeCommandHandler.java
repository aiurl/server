package io.theurl.identity.application.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.TokenRevokeCommand;
import io.theurl.identity.domain.enums.TokenStatus;
import io.theurl.identity.domain.repository.TokenRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TokenRevokeCommandHandler implements Handler<TokenRevokeCommand, Void> {
    private final TokenRepository repository;

    public TokenRevokeCommandHandler(TokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Void> handleAsync(TokenRevokeCommand message) {
        var token = repository.findByJti(message.jti());
        if (token != null) {
            token.revoke(TokenStatus.valueOf(message.reason().toLowerCase()));
            repository.save(token);
        }
        return CompletableFuture.completedFuture(null);
    }
}
