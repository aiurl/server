package io.theurl.identity.persistence.handler;

import com.neroyun.mediator.Handler;
import com.neroyun.mediator.MessageContext;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.persistence.model.OnetimePasswordDetail;
import io.theurl.identity.persistence.query.OnetimePasswordDetailQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OnetimePasswordDetailQueryHandler implements Handler<OnetimePasswordDetailQuery, OnetimePasswordDetail> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Async
    public CompletableFuture<OnetimePasswordDetail> handleAsync(OnetimePasswordDetailQuery message, MessageContext context) {
        var builder = manager.getCriteriaBuilder();
        var criteria = builder.createQuery(OnetimePasswordDetail.class);
        var entity = criteria.from(OnetimePasswordDetail.class);
        criteria.where(builder.equal(entity.get("requestId"), message.requestId()));
        var typedQuery = manager.createQuery(criteria);
        return CompletableFuture.completedFuture(typedQuery.getSingleResult());
    }
}
