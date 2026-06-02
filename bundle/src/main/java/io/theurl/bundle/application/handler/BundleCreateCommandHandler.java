package io.theurl.bundle.application.handler;

import com.neroyun.mediator.Handler;
import com.neroyun.mediator.MessageContext;
import io.theurl.bundle.application.command.BundleCreateCommand;
import io.theurl.bundle.domain.aggregate.Bundle;
import io.theurl.bundle.domain.repository.BundleRepository;
import io.theurl.framework.core.BeanScope;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BundleCreateCommandHandler implements Handler<BundleCreateCommand, Void> {
    private final BundleRepository repository;

    public BundleCreateCommandHandler(BundleRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Void> handleAsync(BundleCreateCommand message, MessageContext context) {
        var userId = Long.parseLong(Objects.requireNonNull(getRequest()).getUserPrincipal().getName());
        var aggregate = Bundle.create(message.getType(), message.getVanity(), message.getName());
        if (message.getDescription() != null) {
            aggregate.setDescription(message.getDescription());
        }
        if (message.getImage() != null) {
            aggregate.setImage(message.getImage());
        }
        aggregate.setOwner(message.getOwnerId(), message.getOwnerName());
        repository.save(aggregate, userId);
        context.onComplete(List.copyOf(aggregate.getEvents()));
        return CompletableFuture.completedFuture(null);
    }

    private HttpServletRequest getRequest() {
        var request = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (request == null) {
            return null;
        }

        return request.getRequest();
    }
}
