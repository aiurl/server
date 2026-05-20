package io.theurl.identity.external;

/**
 * Defines the contract for external authentication providers.
 * Implementations of this interface should handle the authentication process with external services.
 */
public interface ExternalAuthProvider {
    /**
     * Authenticates a user using the provided authentication code.
     * @param authCode The authentication code received from the external service.
     * @return The result of the authentication process encapsulated in an ExternalAuthResult object.
     */
    ExternalAuthResult authenticate(String authCode);
}
