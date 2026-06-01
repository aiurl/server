package io.theurl.bundle.application.contract;

import io.theurl.bundle.application.dto.BundleCreateDto;
import io.theurl.bundle.application.dto.BundleUpdateDto;
import io.theurl.framework.application.ApplicationService;

import java.util.concurrent.CompletableFuture;

public interface BundleApplicationService extends ApplicationService {
    CompletableFuture<String> createAsync(BundleCreateDto data);

    CompletableFuture<Void> updateAsync(String vanity, BundleUpdateDto data);

    CompletableFuture<Void> deleteAsync(String vanity);
}
