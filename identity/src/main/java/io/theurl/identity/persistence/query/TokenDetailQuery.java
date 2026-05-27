package io.theurl.identity.persistence.query;

import com.neroyun.mediator.Query;
import io.theurl.identity.persistence.model.TokenDetail;

public record TokenDetailQuery(String jti) implements Query<TokenDetail> {
}
