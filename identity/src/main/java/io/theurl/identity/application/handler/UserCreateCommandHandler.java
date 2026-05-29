package io.theurl.identity.application.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.UserCreateCommand;
import io.theurl.identity.domain.aggregate.User;
import io.theurl.identity.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(BeanScope.PROTOTYPE)
public class UserCreateCommandHandler implements Handler<UserCreateCommand, Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCreateCommandHandler.class);
    private final UserRepository repository;

    public UserCreateCommandHandler(UserRepository repository) {
        this.repository = repository;
    }

    @Async
    @Transactional
    @Override
    public CompletableFuture<Void> handleAsync(UserCreateCommand message) {
        try {
            var exists = repository.findByAnyOf(message.getUsername(), message.getEmail(), message.getPhone());

            if (exists != null) {
                throw new IllegalArgumentException("User with the same username, email or phone already exists.");
            }

            var user = User.create(message.getUsername(), message.getNickname(), message.getEmail(), message.getPhone());
            user.setPassword(message.getPassword(), "init");
            repository.save(user);
            return CompletableFuture.completedFuture(null);
        } catch (Exception exception) {
            LOGGER.error(exception.getLocalizedMessage(), exception);
            throw exception;
        }
    }
}
