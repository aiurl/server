package io.theurl.identity.application.contract;

import io.theurl.framework.application.ApplicationService;
import io.theurl.identity.application.dto.UserCreateRequestDto;
import io.theurl.identity.application.dto.UserProfileResponseDto;

import java.util.concurrent.CompletableFuture;

/**
 * UserApplicationService defines the contract for user-related operations in the application layer.
 * It provides asynchronous methods for creating users, retrieving user profiles, and updating user information such as password, email, phone number, and nickname.
 * Additionally, it includes methods for connecting and removing authorities associated with the user.
 */
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

    /**
     * Change the email of the currently authenticated user asynchronously.
     *
     * @param email The new email to be set for the user.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    CompletableFuture<Void> changeEmailAsync(String email);

    /**
     * Change the phone number of the currently authenticated user asynchronously.
     *
     * @param phone The new phone number to be set for the user.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    CompletableFuture<Void> changePhoneAsync(String phone);

    /**
     * Change the nickname of the currently authenticated user asynchronously.
     *
     * @param nickname The new nickname to be set for the user.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    CompletableFuture<Void> changeNicknameAsync(String nickname);

    /**
     * Connect an authority to the currently authenticated user asynchronously.
     *
     * @param provider The authority provider to connect.
     * @param code     The authorization code for the authority.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    CompletableFuture<Void> connectAuthorityAsync(String provider, String code);

    /**
     * Remove an authority from the currently authenticated user asynchronously.
     *
     * @param provider The authority provider to remove.
     * @param openId   The open ID of the authority to remove.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    CompletableFuture<Void> removeAuthorityAsync(String provider, String openId);
}
