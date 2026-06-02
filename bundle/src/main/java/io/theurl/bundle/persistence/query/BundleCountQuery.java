package io.theurl.bundle.persistence.query;

import com.neroyun.mediator.Query;

import java.util.Map;

public record BundleCountQuery(Map<String, Object> criteria) implements Query<Integer> {
}
