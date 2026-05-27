package io.theurl.identity.application.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.TokenRevokeCommand;
import io.theurl.identity.domain.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(BeanScope.PROTOTYPE)
public class TokenRevokeCommandHandler implements Handler<TokenRevokeCommand, Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenRevokeCommandHandler.class);

    private final TokenRepository repository;

    public TokenRevokeCommandHandler(TokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Void> handleAsync(TokenRevokeCommand message) {
        try {
            var token = repository.findByJti(message.jti());
            if (token != null) {
                token.revoke(message.status());
                repository.save(token);
            }
            return CompletableFuture.completedFuture(null);
        } catch (Exception exception) {
            LOGGER.error(exception.getLocalizedMessage(), exception);
            throw exception;
        }
    }
}
