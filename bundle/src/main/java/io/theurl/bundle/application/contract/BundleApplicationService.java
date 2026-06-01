package io.theurl.bundle.application.contract;

import io.theurl.bundle.application.dto.BundleCreateDto;
import io.theurl.bundle.application.dto.BundleItemEditDto;
import io.theurl.bundle.application.dto.BundleUpdateDto;
import io.theurl.framework.application.ApplicationService;

import java.util.concurrent.CompletableFuture;

public interface BundleApplicationService extends ApplicationService {

    /**
     * Creates a new bundle with the provided data. If the vanity is not provided, it will be generated automatically.
     *
     * @param data The data for the new bundle.
     * @return A CompletableFuture that will complete with the vanity of the created bundle.
     */
    CompletableFuture<String> createAsync(BundleCreateDto data);

    /**
     * Updates an existing bundle identified by the vanity with the provided data.
     *
     * @param vanity The vanity of the bundle to update.
     * @param data   The data to update the bundle with.
     * @return A CompletableFuture that will complete when the update is done.
     */
    CompletableFuture<Void> updateAsync(String vanity, BundleUpdateDto data);

    /**
     * Deletes the bundle identified by the vanity.
     *
     * @param vanity The vanity of the bundle to delete.
     * @return A CompletableFuture that will complete when the deletion is done.
     */
    CompletableFuture<Void> deleteAsync(String vanity);

    /**
     * Appends an item to the bundle identified by the vanity with the provided data.
     *
     * @param vanity The vanity of the bundle to append the item to.
     * @param data   The data of the item to append.
     * @return A CompletableFuture that will complete when the item is appended.
     */
    CompletableFuture<Void> appendItemAsync(String vanity, BundleItemEditDto data);

    /**
     * Updates an item in the bundle identified by the vanity with the provided data.
     *
     * @param vanity The vanity of the bundle containing the item to update.
     * @param itemId The ID of the item to update.
     * @param data   The data to update the item with.
     * @return A CompletableFuture that will complete when the item is updated.
     */
    CompletableFuture<Void> updateItemAsync(String vanity, long itemId, BundleItemEditDto data);

    /**
     * Removes an item from the bundle identified by the vanity.
     *
     * @param vanity The vanity of the bundle to remove the item from.
     * @param itemId The ID of the item to remove.
     * @return A CompletableFuture that will complete when the item is removed.
     */
    CompletableFuture<Void> removeItemAsync(String vanity, long itemId);
}
