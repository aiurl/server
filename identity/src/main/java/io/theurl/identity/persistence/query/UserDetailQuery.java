package io.theurl.identity.persistence.query;

import com.neroyun.mediator.Query;
import io.theurl.identity.persistence.model.UserDetail;

public record UserDetailQuery(Long id) implements Query<UserDetail> {
}
