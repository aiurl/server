package io.theurl.bundle.domain.repository;

import io.theurl.bundle.domain.aggregate.Bundle;

public interface BundleRepository {
    /**
     * Saves the given bundle to the repository.
     *
     * @param bundle     the bundle to save
     * @param operatorId the identifier of the operator performing the save operation
     */
    void save(Bundle bundle, long operatorId);

    /**
     * Retrieves a bundle by its unique identifier.
     *
     * @param id the unique identifier of the bundle
     * @return the bundle with the specified id, or null if not found
     */
    Bundle findById(Long id);

    /**
     * Retrieves a bundle by its vanity URL.
     *
     * @param vanity the vanity URL of the bundle
     * @return the bundle with the specified vanity URL, or null if not found
     */
    Bundle findByVanity(String vanity);
}
