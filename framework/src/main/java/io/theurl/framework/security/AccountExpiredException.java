package io.theurl.framework.security;

/**
 * Exception thrown when an account has expired and can no longer be used for authentication or access.
 * Carries the identity of the expired account for diagnostics.
 */
@SuppressWarnings("unused")
public class AccountExpiredException extends AccountException {
    public AccountExpiredException(String identity) {
        super(identity);
    }

    public AccountExpiredException(String identity, String message) {
        super(identity, message);
    }

    public AccountExpiredException(String identity, String message, Throwable cause) {
        super(identity, message, cause);
    }
}
