package io.theurl.identity.application.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.UserAuthorityRemoveCommand;
import io.theurl.identity.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserAuthorityRemoveCommandHandler implements Handler<UserAuthorityRemoveCommand, Void> {
    private final UserRepository repository;

    public UserAuthorityRemoveCommandHandler(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Void> handleAsync(UserAuthorityRemoveCommand message) {
        var user = repository.findById(message.id());
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        user.removeAuthority(message.provider(), message.openId());
        repository.save(user);
        return CompletableFuture.completedFuture(null);
    }
}
