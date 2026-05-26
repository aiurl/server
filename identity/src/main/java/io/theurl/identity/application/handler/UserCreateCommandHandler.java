package io.theurl.identity.application.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.UserCreateCommand;
import io.theurl.identity.domain.aggregate.User;
import io.theurl.identity.domain.repository.UserRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserCreateCommandHandler implements Handler<UserCreateCommand, Void> {
    private final UserRepository repository;

    public UserCreateCommandHandler(UserRepository repository) {
        this.repository = repository;
    }

    @Async
    @Transactional
    @Override
    public CompletableFuture<Void> handleAsync(UserCreateCommand message) {
        var exists = repository.findByAnyOf(message.getUsername(), message.getEmail(), message.getPhone());

        if (exists != null) {
            throw new IllegalArgumentException("User with the same username, email or phone already exists.");
        }

        var user = User.create(message.getUsername(), message.getNickname(), message.getEmail(), message.getPhone());
        user.setPassword(message.getPassword(), "init");
        repository.save(user);
        return CompletableFuture.completedFuture(null);
    }
}
