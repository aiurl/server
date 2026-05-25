package io.theurl.identity.persistence.repository;

import io.theurl.identity.persistence.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaTokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByJti(String jti);
}
