package io.theurl.identity.application.dto;

/**
 * DTO for token grant response
 *
 * @param accessToken  the access token for the user, used for authentication and authorization in subsequent requests.
 * @param refreshToken the refresh token for the user, used to obtain a new access token when the current one expires.
 * @param tokenType    the type of the token, typically "Bearer".
 * @param expiresIn    the duration in seconds for which the access token is valid.
 * @param issueAt      the timestamp when the token was issued.
 * @param username     the username of the authenticated user.
 * @param userId       the unique identifier of the authenticated user.
 */
public record TokenGrantResponseDto(String accessToken, String refreshToken, String tokenType, long expiresIn,
                                    long issueAt, String username, Long userId) {
}
