package io.theurl.identity.application.contract;

import io.theurl.identity.application.dto.TokenGrantRequestDto;
import io.theurl.identity.application.dto.TokenGrantResponseDto;

import java.util.concurrent.CompletableFuture;

/**
 * AuthApplicationService defines the contract for authentication-related operations in the application layer. It provides a method to handle token granting requests, which typically involve validating user credentials and issuing access tokens for authenticated users.
 */
public interface AuthApplicationService {
    /**
     * Handle a token granting request asynchronously.
     *
     * @param request The token grant request data.
     * @return A CompletableFuture containing the token grant response data.
     */
    CompletableFuture<TokenGrantResponseDto> grant(TokenGrantRequestDto request);

    /**
     * Revoke a token asynchronously.
     *
     * @param jti The token identifier to revoke.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    CompletableFuture<Void> revoke(String jti);
}
