package io.theurl.identity.application.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.UserAccessFailureCountCommand;
import io.theurl.identity.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(BeanScope.PROTOTYPE)
public class UserAccessFailureCountCommandHandler implements Handler<UserAccessFailureCountCommand, Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccessFailureCountCommandHandler.class);
    private final UserRepository repository;

    public UserAccessFailureCountCommandHandler(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Void> handleAsync(UserAccessFailureCountCommand message) {
        try {
            var user = repository.findById(message.userId());
            if (user == null) {
                return CompletableFuture.completedFuture(null);
            }

            switch (message.action()) {
                case "increase" -> user.increaseAccessFailedCount();
                case "reset" -> user.resetAccessFailedCount();
            }

            repository.save(user);
            return CompletableFuture.completedFuture(null);
        } catch (Exception exception) {
            LOGGER.error(exception.getLocalizedMessage(), exception);
            throw exception;
        }
    }
}
