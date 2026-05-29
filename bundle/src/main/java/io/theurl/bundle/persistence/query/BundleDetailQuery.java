package io.theurl.bundle.persistence.query;

import com.neroyun.mediator.Query;
import io.theurl.bundle.persistence.model.BundleDetailModel;

public record BundleDetailQuery(String vanity) implements Query<BundleDetailModel> {
}
