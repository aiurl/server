package io.theurl.identity.persistence.query;

import com.neroyun.mediator.Query;
import io.theurl.identity.persistence.model.OnetimePasswordDetail;

public record OnetimePasswordDetailQuery(String requestId) implements Query<OnetimePasswordDetail> {
}
