package io.theurl.identity.persistence.repository;

import io.theurl.identity.persistence.entity.Authlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAuthlogRepository extends CrudRepository<Authlog, Long> {
}
