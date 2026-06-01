package io.theurl.bundle.persistence.query;

import com.neroyun.mediator.Query;
import io.theurl.bundle.persistence.model.BundleListModel;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public record BundleListQuery(Map<String, Object> criteria, int from, int size) implements Query<List<BundleListModel>> {

    public void tryGet(String key, Consumer<Object> consumer) {
        if (criteria == null || !criteria.containsKey(key)) {
            return;
        }

        var params = criteria.get(key);

        if (params == null) {
            return;
        }

        consumer.accept(criteria.get(key));
    }
}
