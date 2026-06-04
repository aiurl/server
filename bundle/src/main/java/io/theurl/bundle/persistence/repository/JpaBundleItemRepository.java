package io.theurl.bundle.persistence.repository;

import io.theurl.bundle.persistence.entity.BundleItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("SqlNoDataSourceInspection")
@Repository
public interface JpaBundleItemRepository extends CrudRepository<BundleItem, Long> {

    /**
     * Finds a BundleItem by its ID and the vanity of its associated Bundle, ensuring the Bundle is not marked as deleted.
     *
     * @param vanity The vanity string of the associated Bundle.
     * @param itemId The ID of the BundleItem.
     * @return The BundleItem matching the given ID and vanity, or null if not found.
     */
    @Query("SELECT bi FROM BundleItem bi JOIN bi.bundle b WHERE b.vanity = :vanity AND b.deleted = false AND bi.id = :itemId")
    Optional<BundleItem> findByVanity(String vanity, long itemId);

    /**
     * Finds a BundleItem by its ID and the ID of its associated Bundle, ensuring the Bundle is not marked as deleted.
     *
     * @param bundleId The ID of the associated Bundle.
     * @param itemId   The ID of the BundleItem.
     * @return The BundleItem matching the given IDs, or null if not found.
     */
    @Query("SELECT bi FROM BundleItem bi JOIN bi.bundle b WHERE b.id = :bundleId AND b.deleted = false AND bi.id = :itemId")
    Optional<BundleItem> findByBundleId(Long bundleId, long itemId);

    /**
     * Fetches the top N items for each bundle ID in the provided list, ordered by their 'order' field in descending order.
     *
     * @param bundleIds The list of bundle IDs to fetch items for.
     * @param limit     The maximum number of items to fetch for each bundle.
     * @return A list of top items for each bundle.
     */
    @Query(value = """
        SELECT sub.* FROM (
                SELECT itm.*, ROW_NUMBER() OVER(PARTITION BY itm.bundle_id ORDER BY itm.order DESC) AS row_num FROM bundle_item AS itm WHERE itm.bundle_id IN (:bundleIds)
        ) AS sub
        WHERE sub.row_num <= :limit ORDER BY sub.bundle_id ASC, sub.order DESC
        """, nativeQuery = true)
    List<BundleItem> getTopItems(List<Long> bundleIds, int limit);
}
