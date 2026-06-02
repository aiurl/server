package io.theurl.bundle.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.theurl.bundle.application.contract.BundleApplicationService;
import io.theurl.bundle.application.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Bookmark aggregate HTTP endpoints.
 *
 * <p>All APIs delegate business logic to {@link BundleApplicationService};
 * the controller only handles request mapping, lightweight parameter shaping,
 * and response header composition.</p>
 */
@RestController
@RequestMapping("/api/bookmark")
public class BookmarkController {
    private final BundleApplicationService service;

    public BookmarkController(BundleApplicationService service) {
        this.service = service;
    }

    /**
     * Retrieves a list of owned bookmarks with optional keyword filtering and pagination.
     *
     * @param keyword Optional keyword to filter bookmarks.
     * @param from    Optional starting index for pagination.
     * @param size    Optional number of bookmarks to retrieve.
     * @return A CompletableFuture containing a list of owned bookmarks.
     */
    @GetMapping("/my")
    @Operation(summary = "Get owned bookmarks", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<List<BundleListDto>> getOwnedAsync(@RequestParam(required = false) String keyword,
                                                                @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        // Force owned+bookmark filters; optional keyword is merged only when provided.
        var criteria = new HashMap<>(Map.<String, Object>of("owned", true, "type", "bookmark"));
        if (keyword != null) {
            criteria.put("keyword", keyword);
        }
        return service.searchAsync(criteria, from, size);
    }

    /**
     * Searches bookmarks across the system with optional keyword filtering and pagination.
     *
     * @param keyword Optional keyword to filter bookmarks.
     * @param from    Optional starting index for pagination.
     * @param size    Optional number of bookmarks to retrieve.
     * @return A CompletableFuture containing a list of bookmarks matching the criteria.
     */
    @GetMapping("search")
    @Operation(summary = "Search bookmarks", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<List<BundleListDto>> searchAsync(@RequestParam(required = false) String keyword,
                                                              @RequestParam(required = false, defaultValue = "0") Integer from,
                                                              @RequestParam(required = false, defaultValue = "10") Integer size) {
        // Shared search endpoint constrained to bookmark type.
        var criteria = new HashMap<String, Object>();
        if (keyword != null) {
            criteria.put("keyword", keyword);
        }
        criteria.put("type", "bookmark");
        return service.searchAsync(criteria, from, size);
    }

    /**
     * Creates a new bookmark with the provided data. The server enforces the resource type to "bookmark" to prevent client-side tampering.
     *
     * @param data     The data for the new bookmark.
     * @param response The HTTP response to which headers can be added.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    @PostMapping
    @Operation(summary = "Create a new bookmark", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> createAsync(@RequestBody BundleCreateDto data, HttpServletResponse response) {
        // Enforce server-side resource type to avoid client-side tampering.
        data.setType("bookmark");
        return service.createAsync(data)
                      // Return created vanity identifier through response header for client navigation.
                      .thenAccept(result -> response.addHeader("x-vanity", result));
    }

    /**
     * Updates an existing bookmark identified by the vanity with the provided data. The server enforces the resource type to "bookmark" to prevent client-side tampering.
     *
     * @param vanity The vanity identifier of the bookmark to update.
     * @param data   The data to update the bookmark with.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    @PutMapping("/{vanity}")
    @Operation(summary = "Update an existing bookmark", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> updateAsync(@PathVariable String vanity, @RequestBody BundleUpdateDto data) {
        return service.updateAsync(vanity, data);
    }

    /**
     * Deletes the bookmark identified by the vanity.
     *
     * @param vanity The vanity identifier of the bookmark to delete.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    @DeleteMapping("/{vanity}")
    @Operation(summary = "Delete an existing bookmark", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> deleteAsync(@PathVariable String vanity) {
        return service.deleteAsync(vanity);
    }

    /**
     * Retrieves items of a bookmark with optional keyword filtering and pagination.
     * Item-level filtering currently supports keyword search across name, description, and URL fields; pagination is delegated to the service layer.
     *
     * @param vanity  The vanity identifier of the bookmark.
     * @param keyword The keyword to filter items by.
     * @param from    The starting index for pagination.
     * @param size    The number of items to retrieve.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    @GetMapping("{vanity}/items")
    @Operation(summary = "Get items of a bookmark", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<List<BundleItemListDto>> searchItemsAsync(@PathVariable String vanity,
                                                                       @RequestParam(required = false) String keyword,
                                                                       @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                       @RequestParam(required = false, defaultValue = "10") Integer size) {
        // Item-level filtering currently supports keyword; pagination is delegated to service.
        var criteria = new HashMap<String, Object>();
        if (keyword != null) {
            criteria.put("keyword", keyword);
        }
        return service.searchItemsAsync(vanity, criteria, from, size);
    }

    /**
     * Appends a new item to the bookmark identified by the vanity with the provided data.
     *
     * @param vanity The vanity identifier of the bookmark.
     * @param data   The data of the item to append.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    @PostMapping("{vanity}/items")
    @Operation(summary = "Append items to a bookmark", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> appendItemAsync(@PathVariable String vanity, @RequestBody BundleItemEditDto data) {
        return service.appendItemAsync(vanity, data);
    }

    /**
     * Updates an existing item in the bookmark identified by the vanity with the provided data.
     *
     * @param vanity The vanity identifier of the bookmark.
     * @param itemId The identifier of the item to update.
     * @param data   The data of the item to update.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    @PutMapping("{vanity}/items/{itemId}")
    @Operation(summary = "Update an item in a bookmark", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> updateItemAsync(@PathVariable String vanity, @PathVariable long itemId, @RequestBody BundleItemEditDto data) {
        return service.updateItemAsync(vanity, itemId, data);
    }

    /**
     * Removes an existing item from the bookmark identified by the vanity.
     *
     * @param vanity The vanity identifier of the bookmark.
     * @param itemId The identifier of the item to remove.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    @DeleteMapping("{vanity}/items/{itemId}")
    @Operation(summary = "Delete an item from a bookmark", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> removeItemAsync(@PathVariable String vanity, @PathVariable long itemId) {
        return service.removeItemAsync(vanity, itemId);
    }
}
