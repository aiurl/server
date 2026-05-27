package io.theurl.identity.persistence.handler;

import com.neroyun.mediator.Handler;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.persistence.model.TokenDetail;
import io.theurl.identity.persistence.query.TokenDetailQuery;
import io.theurl.identity.persistence.repository.JpaTokenRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TokenDetailQueryHandler implements Handler<TokenDetailQuery, TokenDetail> {
    private final JpaTokenRepository repository;
    private final ModelMapper mapper;

    public TokenDetailQueryHandler(JpaTokenRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CompletableFuture<TokenDetail> handleAsync(TokenDetailQuery message) {
        var entity = repository.findByJti(message.jti())
                               .orElse(null);

        TokenDetail detail;
        if (entity == null) {
            detail = null;
        } else {
            detail = mapper.map(entity, TokenDetail.class);
        }

        return CompletableFuture.completedFuture(detail);
    }
}
