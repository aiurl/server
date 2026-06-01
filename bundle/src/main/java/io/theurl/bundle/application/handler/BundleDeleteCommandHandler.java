package io.theurl.bundle.application.handler;

import com.neroyun.mediator.Handler;
import com.neroyun.mediator.MessageContext;
import io.theurl.bundle.application.command.BundleDeleteCommand;
import io.theurl.bundle.domain.repository.BundleRepository;
import io.theurl.framework.core.BeanScope;
import io.theurl.framework.security.UnauthorizedAccessException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BundleDeleteCommandHandler implements Handler<BundleDeleteCommand, Void> {
    private final BundleRepository repository;

    public BundleDeleteCommandHandler(BundleRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Void> handleAsync(BundleDeleteCommand message, MessageContext context) {
        var aggregate = repository.findByVanity(message.vanity());
        if (aggregate == null) {
            throw new EntityNotFoundException("Bundle with vanity " + message.vanity() + " not found");
        }

        var userId = Long.getLong(Objects.requireNonNull(getRequest()).getUserPrincipal().getName());

        if (aggregate.getOwnerId() > 0) {
            if (!Objects.equals(aggregate.getOwnerId(), userId)) {
                throw new UnauthorizedAccessException("You are not the owner of this bundle");
            }
        } else {
            if (!getRequest().isUserInRole("ADMIN")) {
                throw new UnauthorizedAccessException("You are not the owner of this bundle");
            }
        }

        aggregate.delete();
        repository.save(aggregate, userId);
        context.onComplete(aggregate.getEvents());
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
