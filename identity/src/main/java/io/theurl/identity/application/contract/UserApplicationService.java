package io.theurl.identity.application.contract;

import io.theurl.framework.application.ApplicationService;
import io.theurl.identity.application.dto.UserCreateRequestDto;
import io.theurl.identity.application.dto.UserProfileResponseDto;

import java.util.concurrent.CompletableFuture;

public interface UserApplicationService extends ApplicationService {

    /**
     * Create a new user asynchronously.
     *
     * @param user The user creation request data.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    CompletableFuture<Void> createAsync(UserCreateRequestDto user);

    /**
     * Get the profile of the currently authenticated user asynchronously.
     *
     * @return A CompletableFuture containing the user's profile data.
     */
    CompletableFuture<UserProfileResponseDto> getProfileAsync();

    /**
     * Change the password of the currently authenticated user asynchronously.
     *
     * @param oldPassword The current password of the user.
     * @param newPassword The new password to be set for the user.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    CompletableFuture<Void> changePasswordAsync(String oldPassword, String newPassword);
}
