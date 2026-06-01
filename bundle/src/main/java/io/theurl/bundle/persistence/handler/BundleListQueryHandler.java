package io.theurl.bundle.persistence.handler;

import com.neroyun.mediator.Handler;
import com.neroyun.mediator.MessageContext;
import io.theurl.bundle.persistence.entity.Bundle;
import io.theurl.bundle.persistence.model.BundleListModel;
import io.theurl.bundle.persistence.query.BundleListQuery;
import io.theurl.framework.core.BeanScope;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BundleListQueryHandler implements Handler<BundleListQuery, List<BundleListModel>> {

    @PersistenceContext
    private EntityManager manager;

    private final ModelMapper mapper;

    public BundleListQueryHandler(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CompletableFuture<List<BundleListModel>> handleAsync(BundleListQuery message, MessageContext context) {
        var builder = manager.getCriteriaBuilder();
        var criteria = builder.createQuery(Bundle.class);
        var entity = criteria.from(Bundle.class);

        var predicates = new ArrayList<>(List.of(builder.isFalse(entity.get("deleted"))));

        message.tryGet("type", v -> predicates.add(builder.equal(entity.get("type"), v)));
        message.tryGet("keyword", v -> predicates.add(builder.like(entity.get("name"), "%" + v + "%")));
        message.tryGet("ownerId", v -> predicates.add(builder.equal(entity.get("ownerId"), v)));

        criteria.where(builder.and(predicates.toArray(new Predicate[0])));
        var typedQuery = manager.createQuery(criteria);
        typedQuery.setFirstResult(message.from());
        typedQuery.setMaxResults(message.size());
        var resultList = typedQuery.getResultList()
                                   .stream()
                                   .map(src -> mapper.map(src, BundleListModel.class))
                                   .toList();
        return CompletableFuture.completedFuture(resultList);
    }
}
