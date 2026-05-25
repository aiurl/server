package io.theurl.identity.persistence.handler;

import com.neroyun.mediator.Handler;
import io.theurl.identity.persistence.model.OnetimePasswordDetail;
import io.theurl.identity.persistence.query.OnetimePasswordDetailQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OnetimePasswordDetailQueryHandler implements Handler<OnetimePasswordDetailQuery, OnetimePasswordDetail> {

    @PersistenceContext
    private EntityManager context;

    @Override
    @Async
    public CompletableFuture<OnetimePasswordDetail> handleAsync(OnetimePasswordDetailQuery message) {
        var builder = context.getCriteriaBuilder();
        var criteria = builder.createQuery(OnetimePasswordDetail.class);
        var entity = criteria.from(OnetimePasswordDetail.class);
        criteria.where(builder.equal(entity.get("requestId"), message.requestId()));
        var typedQuery = context.createQuery(criteria);
        return CompletableFuture.completedFuture(typedQuery.getSingleResult());
    }
}
