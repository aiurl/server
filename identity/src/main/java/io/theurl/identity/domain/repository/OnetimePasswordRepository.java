package io.theurl.identity.domain.repository;

import io.theurl.identity.domain.aggregate.OnetimePassword;

public interface OnetimePasswordRepository {
    void save(OnetimePassword aggregate);

    OnetimePassword findById(Long id);

    OnetimePassword findByRequestId(String requestId);
}
