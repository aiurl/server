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
        var query = builder.createQuery(Bundle.class);
        var select = query.from(Bundle.class);

        List<Predicate> predicates = new ArrayList<>(List.of(builder.isFalse(select.get("deleted"))));

        message.criteria().forEach((k, v) -> {
            switch (k) {
                case "ownerId" -> predicates.add(builder.equal(select.get("ownerId"), v));
                case "type" -> predicates.add(builder.equal(select.get("type"), v));
                case "keyword" -> {
                    if (v instanceof String keyword) {
                        Predicate orGroup = builder.or(
                            builder.like(select.get("name"), "%" + keyword + "%"),
                            builder.like(select.get("description"), "%" + keyword + "%")
                        );
                        predicates.add(orGroup);
                    }
                }
                default -> {
                    //predicates.add(builder.equal(select.get(k), v));
                }
            }
        });

        query.where(builder.and(predicates.toArray(new Predicate[0])));

        var typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult(message.from());
        typedQuery.setMaxResults(message.size());
        var resultList = typedQuery.getResultList()
                                   .stream()
                                   .map(src -> mapper.map(src, BundleListModel.class))
                                   .toList();
        return CompletableFuture.completedFuture(resultList);
    }
}
