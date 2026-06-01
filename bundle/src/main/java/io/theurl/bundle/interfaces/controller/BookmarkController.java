package io.theurl.bundle.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.theurl.bundle.application.contract.BundleApplicationService;
import io.theurl.bundle.application.dto.BundleCreateDto;
import io.theurl.bundle.application.dto.BundleItemListDto;
import io.theurl.bundle.application.dto.BundleListDto;
import io.theurl.bundle.application.dto.BundleUpdateDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/bookmark")
public class BookmarkController {
    private final BundleApplicationService service;

    public BookmarkController(BundleApplicationService service) {
        this.service = service;
    }

    @GetMapping("/my")
    @Operation(summary = "Get owned bookmarks", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<List<BundleListDto>> getOwnedAsync(@RequestParam(required = false) String keyword,
                                                                @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        var criteria = new HashMap<>(Map.<String, Object>of("owned", true, "type", "bookmark"));
        if (keyword != null) {
            criteria.put("keyword", keyword);
        }
        return service.searchAsync(criteria, from, size);
    }

    @PostMapping
    @Operation(summary = "Create a new bookmark", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> createAsync(@RequestBody BundleCreateDto data, HttpServletResponse response) {
        data.setType("bookmark");
        return service.createAsync(data)
                      .thenAccept(result -> response.addHeader("x-vanity", result));
    }

    @PutMapping("/{vanity}")
    @Operation(summary = "Update an existing bookmark", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> updateAsync(@PathVariable String vanity, @RequestBody BundleUpdateDto data) {
        return service.updateAsync(vanity, data);
    }

    @DeleteMapping("/{vanity}")
    @Operation(summary = "Delete an existing bookmark", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Void> deleteAsync(@PathVariable String vanity) {
        return service.deleteAsync(vanity);
    }

    @GetMapping("{vanity}/items/list")
    @Operation(summary = "Get items of a bookmark", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<List<BundleItemListDto>> searchItemsAsync(@PathVariable String vanity,
                                                                       @RequestParam(required = false) String keyword,
                                                                       @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                       @RequestParam(required = false, defaultValue = "10") Integer size) {
        var criteria = new HashMap<String, Object>();
        if (keyword != null) {
            criteria.put("keyword", keyword);
        }
        return service.searchItemsAsync(vanity, criteria, from, size);
    }

    @GetMapping("{vanity}/items/count")
    @Operation(summary = "Get count of items in a bookmark", security = @SecurityRequirement(name = "bearerAuth"))
    public CompletableFuture<Integer> countItemsAsync(@PathVariable String vanity,
                                                      @RequestParam(required = false) String keyword) {
        var criteria = new HashMap<String, Object>();
        if (keyword != null) {
            criteria.put("keyword", keyword);
        }
        return service.countItemsAsync(vanity, criteria);
    }
}
