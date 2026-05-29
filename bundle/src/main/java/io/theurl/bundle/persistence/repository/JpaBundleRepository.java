package io.theurl.bundle.persistence.repository;

import io.theurl.bundle.persistence.entity.Bundle;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JpaBundleRepository extends CrudRepository<Bundle, Long> {
    Optional<Bundle> findByVanity(String vanity);
}
