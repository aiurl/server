package io.theurl.identity.persistence.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.persistence.model.UserDetail;
import io.theurl.identity.persistence.query.UserDetailQuery;
import io.theurl.identity.persistence.repository.JpaUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(BeanScope.PROTOTYPE)
public class UserDetailQueryHandler implements Handler<UserDetailQuery, UserDetail> {
    private final JpaUserRepository repository;
    private final ModelMapper mapper;

    public UserDetailQueryHandler(JpaUserRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CompletableFuture<UserDetail> handleAsync(UserDetailQuery message) {
        var user = repository.findById(message.id()).orElse(null);

        UserDetail detail;

        if (user == null) {
            detail = null;
        } else {
            detail = mapper.map(user, UserDetail.class);
        }

        return CompletableFuture.completedFuture(detail);
    }
}
