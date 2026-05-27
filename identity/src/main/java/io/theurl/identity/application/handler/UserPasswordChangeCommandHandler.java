package io.theurl.identity.application.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.UserPasswordChangeCommand;
import io.theurl.identity.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(BeanScope.PROTOTYPE)
public class UserPasswordChangeCommandHandler implements Handler<UserPasswordChangeCommand, Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserPasswordChangeCommandHandler.class);
    private final UserRepository repository;

    public UserPasswordChangeCommandHandler(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Void> handleAsync(UserPasswordChangeCommand message) {
        try {
            var user = repository.findById(message.userId());
            if (user == null) {
                return CompletableFuture.completedFuture(null);
            }

            user.setPassword(message.password(), message.changeType());
            repository.save(user);
            return CompletableFuture.completedFuture(null);
        } catch (Exception exception) {
            LOGGER.error(exception.getLocalizedMessage(), exception);
            throw exception;
        }
    }
}
