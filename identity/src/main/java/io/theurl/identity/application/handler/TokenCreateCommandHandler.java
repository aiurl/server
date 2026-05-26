package io.theurl.identity.application.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.TokenCreateCommand;
import io.theurl.identity.domain.aggregate.Token;
import io.theurl.identity.domain.repository.TokenRepository;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TokenCreateCommandHandler implements Handler<TokenCreateCommand, Void> {

    @Resource
    private TokenRepository tokenRepository;

    @Override
    public CompletableFuture<Void> handleAsync(TokenCreateCommand message) {
        var token = Token.create(message.getJti(), message.getContent(), message.getSubject());
        token.setExpiresAt(message.getExpiresAt());
        token.setIssuedAt(message.getIssuedAt());
        tokenRepository.save(token);
        return CompletableFuture.completedFuture(null);
    }
}
