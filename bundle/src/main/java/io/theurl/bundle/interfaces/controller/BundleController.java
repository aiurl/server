package io.theurl.bundle.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.theurl.bundle.application.contract.BundleApplicationService;
import io.theurl.bundle.application.dto.BundleCreateDto;
import io.theurl.bundle.application.dto.BundleItemEditDto;
import io.theurl.bundle.application.dto.BundleUpdateDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/bundles")
public class BundleController {

    private final BundleApplicationService service;

    public BundleController(BundleApplicationService service) {
        this.service = service;
    }

    /**
     * Create a new bundle based on the provided data.
     * This endpoint allows clients to create a new bundle by providing the necessary data in the request body.
     * The server will process the request and, if the data is valid, will create a new bundle in the system.
     * Upon successful creation, the server will return a response with a header containing the vanity URL of the newly created bundle, which can be used for future reference and access to the bundle.
     *
     * @param data     The data for the new bundle.
     * @param response The HTTP response.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    @PostMapping
    @Operation(summary = "Create a new bundle", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> create(@RequestBody BundleCreateDto data, HttpServletResponse response) {
        return service.createAsync(data)
                      .thenAccept(vanity -> response.addHeader("x-vanity", vanity));
    }

    /**
     * Update an existing bundle identified by the vanity URL with the provided data.
     * This endpoint allows clients to update the details of an existing bundle by providing the vanity URL as a path variable and the updated data in the request body.
     * The server will process the request and, if the vanity URL is valid and the data is acceptable, will update the corresponding bundle in the system.
     * The server will return a response indicating the success or failure of the update operation.
     *
     * @param vanity The vanity URL of the bundle to be updated.
     * @param data   The updated data for the bundle.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    @PutMapping("/{vanity}")
    @Operation(summary = "Update an existing bundle", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> update(@PathVariable String vanity, @RequestBody BundleUpdateDto data) {
        return service.updateAsync(vanity, data);
    }

    /**
     * Delete an existing bundle identified by the vanity URL.
     * This endpoint allows clients to delete an existing bundle by providing the vanity URL as a path variable.
     * The server will process the request and, if the vanity URL is valid, will remove the corresponding bundle from the system.
     * The server will return a response indicating the success or failure of the delete operation.
     *
     * @param vanity The vanity URL of the bundle to be deleted.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    @DeleteMapping("/{vanity}")
    @Operation(summary = "Delete an existing bundle", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> delete(@PathVariable String vanity) {
        return service.deleteAsync(vanity);
    }

    /**
     * Append an item to an existing bundle identified by the vanity URL with the provided data.
     * This endpoint allows clients to add a new item to an existing bundle by providing the vanity URL as a path variable and the item data in the request body.
     * The server will process the request and, if the vanity URL is valid and the item data is acceptable, will append the item to the corresponding bundle in the system.
     * The server will return a response indicating the success or failure of the append operation.
     *
     * @param vanity The vanity URL of the bundle to which the item will be appended.
     * @param data   The data for the item to be appended.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    @PostMapping("/{vanity}/append")
    @Operation(summary = "Append items to an existing bundle", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> append(@PathVariable String vanity, @RequestBody BundleItemEditDto data) {
        return service.appendItemAsync(vanity, data);
    }

    /**
     * Remove an item from an existing bundle identified by the vanity URL and item ID.
     * This endpoint allows clients to remove an existing item from a bundle by providing the vanity URL as a path variable and the item ID as another path variable.
     * The server will process the request and, if the vanity URL and item ID are valid, will remove the corresponding item from the bundle in the system.
     * The server will return a response indicating the success or failure of the remove operation.
     *
     * @param vanity The vanity URL of the bundle from which the item will be removed.
     * @param itemId The ID of the item to be removed.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    @DeleteMapping("/{vanity}/{itemId}")
    @Operation(summary = "Remove an item from an existing bundle", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> remove(@PathVariable String vanity, @PathVariable long itemId) {
        return service.removeItemAsync(vanity, itemId);
    }

    /**
     * Update an item in an existing bundle identified by the vanity URL and item ID with the provided data.
     * This endpoint allows clients to update the details of an existing item in a bundle by providing the vanity URL as a path variable, the item ID as another path variable, and the updated item data in the request body.
     * The server will process the request and, if the vanity URL, item ID, and updated item data are valid, will update the corresponding item in the bundle in the system.
     * The server will return a response indicating the success or failure of the update operation.
     *
     * @param vanity The vanity URL of the bundle containing the item to be updated.
     * @param itemId The ID of the item to be updated.
     * @param data   The updated data for the item.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    @PutMapping("/{vanity}/{itemId}")
    @Operation(summary = "Update an item in an existing bundle", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> updateItem(@PathVariable String vanity, @PathVariable long itemId, @RequestBody BundleItemEditDto data) {
        return service.updateItemAsync(vanity, itemId, data);
    }
}
