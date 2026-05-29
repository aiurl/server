package io.theurl.identity.application.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.UserUpdateCommand;
import io.theurl.identity.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(BeanScope.PROTOTYPE)
public class UserUpdateCommandHandler implements Handler<UserUpdateCommand, Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserUpdateCommandHandler.class);
    private final UserRepository repository;

    public UserUpdateCommandHandler(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Void> handleAsync(UserUpdateCommand message) {
        try {
            var user = repository.findById(message.getId());

            if (user == null) {
                throw new EntityNotFoundException("User with ID " + message.getId() + " not found");
            }

            message.getModifications().forEach((key, value) -> {
                switch (key) {
                    case "email" -> user.setEmail((String) value);
                    case "phone" -> user.setPhone((String) value);
                    case "nickname" -> user.setNickname((String) value);
                    default -> throw new IllegalArgumentException("Unsupported modification key: " + key);
                }
            });
            repository.save(user);
            return CompletableFuture.completedFuture(null);
        } catch (Exception exception) {
            LOGGER.error(exception.getLocalizedMessage(), exception);
            throw exception;
        }
    }
}
