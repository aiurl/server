package io.theurl.identity.domain.repository;


import io.theurl.identity.domain.aggregate.Authlog;

public interface AuthlogRepository {
    void save(Authlog authlog);
}
