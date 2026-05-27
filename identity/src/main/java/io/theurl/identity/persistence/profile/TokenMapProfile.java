package io.theurl.identity.persistence.profile;

import io.theurl.identity.domain.aggregate.Token;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenMapProfile {
    @Autowired
    private ModelMapper mapper;

    @PostConstruct
    public void configure() {
        Provider<Token> tokenProvider = request -> {
            var source = (io.theurl.identity.persistence.entity.Token) request.getSource();
            return new Token(source.getId(), source.getJti(), source.getContent(), source.getSubject());
        };

        mapper.createTypeMap(io.theurl.identity.persistence.entity.Token.class, Token.class)
              .setProvider(tokenProvider)
              .addMappings(expression -> {
                  expression.map(io.theurl.identity.persistence.entity.Token::getExpiresAt, Token::setExpiresAt);
                  expression.map(io.theurl.identity.persistence.entity.Token::getIssuedAt, Token::setIssuedAt);
                  expression.map(io.theurl.identity.persistence.entity.Token::getRevokedAt, Token::setRevokedAt);
                  expression.map(io.theurl.identity.persistence.entity.Token::getStatus, Token::setStatus);
              });
    }
}
