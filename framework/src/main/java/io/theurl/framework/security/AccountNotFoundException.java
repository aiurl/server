package io.theurl.framework.security;

/**
 * Exception thrown when an account with the specified identity cannot be found.
 * Carries the identity of the missing account for diagnostics.
 */
@SuppressWarnings("unused")
public class AccountNotFoundException extends AccountException {
    public AccountNotFoundException(String identity) {
        super(identity);
    }

    public AccountNotFoundException(String identity, String message) {
        super(identity, message);
    }

    public AccountNotFoundException(String identity, String message, Throwable cause) {
        super(identity, message, cause);
    }
}
