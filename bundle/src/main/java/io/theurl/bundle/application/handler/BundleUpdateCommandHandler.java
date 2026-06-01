package io.theurl.bundle.application.handler;

import com.neroyun.mediator.Handler;
import com.neroyun.mediator.MessageContext;
import io.theurl.bundle.application.command.BundleUpdateCommand;
import io.theurl.bundle.domain.repository.BundleRepository;
import io.theurl.framework.core.BeanScope;
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
public class BundleUpdateCommandHandler implements Handler<BundleUpdateCommand, Void> {

    private final BundleRepository repository;

    public BundleUpdateCommandHandler(BundleRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Void> handleAsync(BundleUpdateCommand message, MessageContext context) {
        var aggregate = repository.findByVanity(message.getVanity());

        if (aggregate == null) {
            throw new EntityNotFoundException("Bundle with vanity '" + message.getVanity() + "' not found.");
        }

        aggregate.setName(message.getName());
        aggregate.setDescription(message.getDescription());
        aggregate.setImage(message.getImage());

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
