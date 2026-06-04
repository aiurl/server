package io.theurl.bundle.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.theurl.bundle.application.contract.BundleApplicationService;
import io.theurl.bundle.application.dto.BundleItemEditDto;
import io.theurl.bundle.application.dto.BundleItemListDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Frequency endpoint for managing the current user's frequently used bundle items.
 * <p>
 * The controller only handles HTTP routing and delegates all business behavior to
 * {@link BundleApplicationService}.
 */
@RestController
@RequestMapping("/api/frequency")
public class FrequencyController {

    private final BundleApplicationService service;

    /**
     * Creates a controller with the application service used for frequency operations.
     *
     * @param service bundle application service
     */
    public FrequencyController(BundleApplicationService service) {
        this.service = service;
    }

    /**
     * Returns all frequency items for the current user.
     *
     * @param request HTTP request used to resolve the current user's vanity key
     * @return async list of frequency items
     */
    @GetMapping("items")
    @Operation(summary = "Get the frequency of items in the bundle", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<List<BundleItemListDto>> fetchItemsAsync(HttpServletRequest request) {
        Map<String, Object> criteria = Map.of("type", "frequency");

        var vanity = getVanity(request);
        return service.searchItemsAsync(vanity, criteria, 0, Integer.MAX_VALUE);
    }

    /**
     * Appends a new item into the current user's frequency list.
     *
     * @param request HTTP request used to resolve the current user's vanity key
     * @param data    item payload to append
     * @return async completion signal
     */
    @PostMapping("items")
    @Operation(summary = "Append item to the frequency list", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> appendItemAsync(HttpServletRequest request, @RequestBody BundleItemEditDto data) {
        var vanity = getVanity(request);
        return service.appendItemAsync(vanity, data);
    }

    /**
     * Updates one item in the current user's frequency list.
     *
     * @param request HTTP request used to resolve the current user's vanity key
     * @param itemId  target item id
     * @param data    updated item payload
     * @return async completion signal
     */
    @PutMapping("items/{itemId}")
    @Operation(summary = "Update an item in the frequency list", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> updateItemAsync(HttpServletRequest request, @PathVariable long itemId, @RequestBody BundleItemEditDto data) {
        var vanity = getVanity(request);
        return service.updateItemAsync(vanity, itemId, data);
    }

    /**
     * Removes one item from the current user's frequency list.
     *
     * @param request HTTP request used to resolve the current user's vanity key
     * @param itemId  target item id
     * @return async completion signal
     */
    @DeleteMapping("items/{itemId}")
    @Operation(summary = "Delete an item from the frequency list", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> removeItemAsync(HttpServletRequest request, @PathVariable long itemId) {
        var vanity = getVanity(request);
        return service.removeItemAsync(vanity, itemId);
    }

    /**
     * Builds the user's frequency bundle vanity.
     * <p>
     * Falls back to a system vanity if user identity is absent.
     *
     * @param request HTTP request carrying authenticated principal
     * @return vanity key used by frequency bundle storage
     */
    private String getVanity(HttpServletRequest request) {
        var userId = request.getUserPrincipal().getName();
        if (StringUtils.hasText(userId)) {
            return "frequency-" + userId;
        } else {
            return "frequency-system";
        }
    }
}
