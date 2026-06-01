package io.theurl.bundle.persistence.handler;

import com.neroyun.mediator.Handler;
import com.neroyun.mediator.MessageContext;
import io.theurl.bundle.persistence.entity.Bundle;
import io.theurl.bundle.persistence.entity.BundleItem;
import io.theurl.bundle.persistence.model.BundleItemModel;
import io.theurl.bundle.persistence.query.BundleItemCountQuery;
import io.theurl.framework.core.BeanScope;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BundleItemCountQueryHandler implements Handler<BundleItemCountQuery, Integer> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public CompletableFuture<Integer> handleAsync(BundleItemCountQuery message, MessageContext context) {
        var builder = manager.getCriteriaBuilder();
        var query = builder.createQuery(Long.class);
        var select = query.from(BundleItem.class);
        query.select(builder.count(select));
        Join<BundleItem, Bundle> join = select.join(Bundle.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.isFalse(join.get("deleted")));
        predicates.add(builder.equal(join.get("vanity"), message.vanity()));

        message.criteria().forEach((k, v) -> {
            if (Objects.equals(k, "keyword") && v instanceof String keyword) {
                Predicate orGroup = builder.or(
                    builder.like(select.get("name"), "%" + keyword + "%"),
                    builder.like(select.get("description"), "%" + keyword + "%"),
                    builder.like(select.get("url"), "%" + keyword + "%")
                );
                predicates.add(orGroup);
            }
        });

        query.where(builder.and(predicates.toArray(new Predicate[0])));
        if (!predicates.isEmpty()) {
            query.where(builder.or(predicates.toArray(new Predicate[0])));
        }

        var typedQuery = manager.createQuery(query);
        var count = typedQuery.getSingleResult();

        return CompletableFuture.completedFuture(count.intValue());
    }
}
