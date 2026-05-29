package io.theurl.identity.application.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.UserAuthorityCreateCommand;
import io.theurl.identity.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(BeanScope.PROTOTYPE)
public class UserAuthorityCreateCommandHandler implements Handler<UserAuthorityCreateCommand, Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthorityCreateCommandHandler.class);
    private final UserRepository repository;

    public UserAuthorityCreateCommandHandler(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Void> handleAsync(UserAuthorityCreateCommand message) {
        try {
            var user = repository.findById(message.id());
            if (user == null) {
                throw new EntityNotFoundException("User not found");
            }

            user.addAuthority(message.provider(), message.openId(), message.name());
            repository.save(user);
            return CompletableFuture.completedFuture(null);
        } catch (Exception exception) {
            LOGGER.error(exception.getLocalizedMessage(), exception);
            throw exception;
        }
    }
}
