package io.theurl.bundle.persistence.handler;

import com.neroyun.mediator.Handler;
import com.neroyun.mediator.MessageContext;
import io.theurl.bundle.persistence.entity.Bundle;
import io.theurl.bundle.persistence.query.BundleCountQuery;
import io.theurl.framework.core.BeanScope;
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
        var query = builder.createQuery(Long.class);
        Root<Bundle> select = query.from(Bundle.class);
        query.select(builder.count(select));
        List<Predicate> predicates = new ArrayList<>(List.of(builder.isFalse(select.get("deleted"))));

        message.criteria().forEach((key, value) -> {
            switch (key) {
                case "ownerId" -> predicates.add(builder.equal(select.get("ownerId"), value));
                case "type" -> predicates.add(builder.equal(select.get("type"), value));
                case "keyword" -> {
                    if (value instanceof String keyword) {
                        Predicate orGroup = builder.or(
                            builder.like(select.get("name"), "%" + keyword + "%"),
                            builder.like(select.get("description"), "%" + keyword + "%")
                        );
                        predicates.add(orGroup);
                    }
                }
                default -> {
                }
            }
        });

        query.where(builder.and(predicates.toArray(new Predicate[0])));
        var typedQuery = manager.createQuery(query);
        var result = typedQuery.getSingleResult();
        return CompletableFuture.completedFuture(result.intValue());
    }
}
