package io.theurl.identity.application.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.AuthlogCreateCommand;
import io.theurl.identity.domain.repository.AuthlogRepository;
import io.theurl.identity.domain.aggregate.Authlog;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthlogCreateCommandHandler implements Handler<AuthlogCreateCommand, Void> {

    private final AuthlogRepository repository;
    private final ModelMapper mapper;

    public AuthlogCreateCommandHandler(AuthlogRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CompletableFuture<Void> handleAsync(AuthlogCreateCommand message) {

        var authlog = Authlog.create(message.getRequestId(), message.getUsername(), message.isSuccess());
        mapper.map(message, authlog);
        repository.save(authlog);
        return CompletableFuture.completedFuture(null);
    }
}
