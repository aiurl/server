package io.theurl.bundle.persistence.query;

import com.neroyun.mediator.Query;
import io.theurl.bundle.persistence.model.BundleItemModel;

import java.util.List;
import java.util.Map;

public record BundleItemListQuery(String vanity, Map<String, Object> criteria, int from,
                                  int size) implements Query<List<BundleItemModel>> {
}
