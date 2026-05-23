package io.theurl.identity.persistence.query;

import com.neroyun.mediator.Query;
import io.theurl.identity.persistence.model.UserAuthInfo;

public record UserAuthInfoQuery(String provider, String name) implements Query<UserAuthInfo> {

}
