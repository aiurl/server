package io.theurl.identity.domain.repository;

import io.theurl.identity.domain.aggregate.Token;

public interface TokenRepository {
    void save(Token token);

    Token findById(Long id);

    Token findByJti(String jti);
}
