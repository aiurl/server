package io.theurl.identity.application.dto;

/**
 * DTO for token grant request
 * @param username the identifier of the user, maybe email, phone number, unique username, etc.
 * @param password the password of the user
 * @param grantType the type of grant, available values: Username/Email/Phone/Github/Microsoft/Google.
 * @param requestId the request ID for the authentication request.
 */
public record TokenGrantRequestDto(String username, String password, String grantType, String requestId) {
}
