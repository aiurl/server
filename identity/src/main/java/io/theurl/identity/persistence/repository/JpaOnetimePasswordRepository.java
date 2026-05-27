package io.theurl.identity.persistence.repository;

import io.theurl.identity.persistence.entity.OnetimePassword;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaOnetimePasswordRepository extends CrudRepository<OnetimePassword, Long> {
    Optional<OnetimePassword> findByRequestId(String requestId);
}
