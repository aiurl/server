package io.theurl.identity.persistence.repository;

import io.theurl.identity.domain.repository.TokenRepository;
import io.theurl.identity.persistence.entity.Token;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final JpaTokenRepository repository;
    private final ModelMapper mapper;

    @Override
    public void save(io.theurl.identity.domain.aggregate.Token token) {
        Token tokenEntity = mapper.map(token, Token.class);
        repository.save(tokenEntity);
    }

    @Override
    public io.theurl.identity.domain.aggregate.Token findById(Long id) {
        return repository.findById(id)
                         .map(tokenEntity -> mapper.map(tokenEntity, io.theurl.identity.domain.aggregate.Token.class))
                         .orElse(null);
    }

    @Override
    public io.theurl.identity.domain.aggregate.Token findByJti(String jti) {
        return repository.findByJti(jti)
                         .map(tokenEntity -> mapper.map(tokenEntity, io.theurl.identity.domain.aggregate.Token.class))
                         .orElse(null);
    }
}
