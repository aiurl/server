package io.theurl.identity.application.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.AuthlogCreateCommand;
import io.theurl.identity.domain.repository.AuthlogRepository;
import io.theurl.identity.domain.aggregate.Authlog;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(BeanScope.PROTOTYPE)
public class AuthlogCreateCommandHandler implements Handler<AuthlogCreateCommand, Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthlogCreateCommandHandler.class);
    private final AuthlogRepository repository;
    private final ModelMapper mapper;

    public AuthlogCreateCommandHandler(AuthlogRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CompletableFuture<Void> handleAsync(AuthlogCreateCommand message) {
        try {
            var authlog = Authlog.create(message.getRequestId(), message.getUsername(), message.isSuccess());
            mapper.map(message, authlog);
            repository.save(authlog);
            return CompletableFuture.completedFuture(null);
        } catch (Exception exception) {
            LOGGER.error(exception.getLocalizedMessage(), exception);
            throw exception;
        }
    }
}
