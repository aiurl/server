package io.theurl.identity.application.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.UserAccessFailureCountCommand;
import io.theurl.identity.domain.repository.UserRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserAccessFailureCountCommandHandler implements Handler<UserAccessFailureCountCommand, Void> {

    private final UserRepository repository;

    public UserAccessFailureCountCommandHandler(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Void> handleAsync(UserAccessFailureCountCommand message) {
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
    }
}
