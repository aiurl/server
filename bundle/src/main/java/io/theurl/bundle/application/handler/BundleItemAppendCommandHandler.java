package io.theurl.bundle.application.handler;

import com.neroyun.mediator.Handler;
import com.neroyun.mediator.MessageContext;
import io.theurl.bundle.application.command.BundleItemAppendCommand;
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

import java.util.concurrent.CompletableFuture;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BundleItemAppendCommandHandler implements Handler<BundleItemAppendCommand, Void> {
    private final BundleRepository repository;

    public BundleItemAppendCommandHandler(BundleRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Void> handleAsync(BundleItemAppendCommand message, MessageContext context) {
        var aggregate = repository.findByVanity(message.getVanity());
        if (aggregate == null) {
            throw new EntityNotFoundException("Bundle not found for vanity: " + message.getVanity());
        }

        var userId = getUserId();
        var request = getRequest();

        assert request != null;

        if (!aggregate.getOwnerId().equals(userId)) {
            if (aggregate.getOwnerId() == null && request.isUserInRole("ADMIN")) {
                throw new UnauthorizedAccessException("You are not allowed to complete this operation");
            }
        }

        aggregate.appendItem(message.getUrl(), message.getTitle(), message.getDescription(), message.getImage());
        repository.save(aggregate, getUserId());
        return CompletableFuture.completedFuture(null);
    }

    private HttpServletRequest getRequest() {
        var request = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (request == null) {
            return null;
        }

        return request.getRequest();
    }

    private long getUserId() {
        var request = getRequest();

        if (request == null || request.getUserPrincipal() == null) {
            return 0;
        }

        return Long.parseLong(request.getUserPrincipal().getName());
    }
}
