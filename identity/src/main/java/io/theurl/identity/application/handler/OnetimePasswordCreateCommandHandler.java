package io.theurl.identity.application.handler;

import com.neroyun.mediator.Event;
import com.neroyun.mediator.Handler;
import com.neroyun.mediator.Mediator;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.OnetimePasswordCreateCommand;
import io.theurl.identity.domain.aggregate.OnetimePassword;
import io.theurl.identity.domain.repository.OnetimePasswordRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(BeanScope.PROTOTYPE)
public class OnetimePasswordCreateCommandHandler implements Handler<OnetimePasswordCreateCommand, Void> {
    private final OnetimePasswordRepository repository;
    private final Mediator mediator;

    public OnetimePasswordCreateCommandHandler(OnetimePasswordRepository repository, Mediator mediator) {
        this.repository = repository;
        this.mediator = mediator;
    }

    @Override
    public CompletableFuture<Void> handleAsync(OnetimePasswordCreateCommand message) {
        var aggregate = OnetimePassword.create(message.getRequestId(), message.getRecipient(), message.getCode(), message.getDuration());
        aggregate.setUsage(message.getUsage());
        repository.save(aggregate);
        if (aggregate.hasEvents()) {
            aggregate.getEvents().parallelStream().forEach(event -> mediator.publishAsync((Event) event));
        }
        return CompletableFuture.completedFuture(null);
    }
}
