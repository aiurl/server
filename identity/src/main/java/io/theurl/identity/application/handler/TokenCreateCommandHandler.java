package io.theurl.identity.application.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.TokenCreateCommand;
import io.theurl.identity.domain.aggregate.Token;
import io.theurl.identity.domain.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(BeanScope.PROTOTYPE)
public class TokenCreateCommandHandler implements Handler<TokenCreateCommand, Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenCreateCommandHandler.class);

    private final TokenRepository repository;

    public TokenCreateCommandHandler(TokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Void> handleAsync(TokenCreateCommand message) {
        try {


            var token = Token.create(message.getJti(), message.getContent(), message.getSubject());
            token.setExpiresAt(message.getExpiresAt());
            token.setIssuedAt(message.getIssuedAt());
            repository.save(token);
            return CompletableFuture.completedFuture(null);
        } catch (Exception exception) {
            LOGGER.error(exception.getLocalizedMessage(), exception);
            throw exception;
        }
    }
}
