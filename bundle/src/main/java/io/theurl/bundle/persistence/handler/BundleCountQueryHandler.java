package io.theurl.bundle.persistence.handler;

import com.neroyun.mediator.Handler;
import com.neroyun.mediator.MessageContext;
import io.theurl.bundle.persistence.entity.Bundle;
import io.theurl.bundle.persistence.query.BundleCountQuery;
import io.theurl.framework.core.BeanScope;
import io.theurl.framework.utility.MapUtility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BundleCountQueryHandler implements Handler<BundleCountQuery, Integer> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public CompletableFuture<Integer> handleAsync(BundleCountQuery message, MessageContext context) {
        var builder = manager.getCriteriaBuilder();
        var criteria = builder.createQuery(Long.class);
        Root<Bundle> entity = criteria.from(Bundle.class);
        criteria.select(builder.count(entity));
        var predicates = new ArrayList<>(List.of(builder.isFalse(entity.get("deleted"))));

        MapUtility.tryGet(message.criteria(), "type", v -> predicates.add(builder.equal(entity.get("type"), v)));
        MapUtility.tryGet(message.criteria(), "keyword", v -> predicates.add(builder.like(entity.get("name"), "%" + v + "%")));
        MapUtility.tryGet(message.criteria(), "ownerId", v -> predicates.add(builder.equal(entity.get("ownerId"), v)));

        criteria.where(builder.and(predicates.toArray(new Predicate[0])));
        var typedQuery = manager.createQuery(criteria);
        var result = typedQuery.getSingleResult();
        return CompletableFuture.completedFuture(result.intValue());
    }
}
