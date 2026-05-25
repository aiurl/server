package io.theurl.framework.security;

/**
 * Exception thrown when an account is locked ant cannot be used for authentication or authorization processes. This typically occurs after multiple failed login attempts or due to administrative actions.
 * The exception includes the identity of the locked account and can be extended to include additional details as needed.
 */
@SuppressWarnings("unused")
public class AccountLockedException extends AccountException {
    public AccountLockedException(Object identity) {
        super(identity);
    }

    public AccountLockedException(Object identity, String message) {
        super(identity, message);
    }

    public AccountLockedException(Object identity, String message, Throwable cause) {
        super(identity, message, cause);
    }
}
