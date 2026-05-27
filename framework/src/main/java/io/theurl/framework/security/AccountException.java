package io.theurl.framework.security;

import java.util.Collections;
import java.util.Map;

/**
 * Exception thrown for account-related errors during authentication or authorization processes, such as account not found, account locked, etc.
 * This exception can be extended to provide more specific error types and include additional details as needed.
 */
@SuppressWarnings("unused")
public class AccountException extends RuntimeException {
    private final Object identity;

    private final Map<String, Object> details = Collections.emptyMap();

    public AccountException(Object identity) {
        this.identity = identity;
    }

    public AccountException(Object identity, String message) {
        super(message);
        this.identity = identity;
    }

    public AccountException(Object identity, String message, Throwable cause) {
        super(message, cause);
        this.identity = identity;
    }

    public Object getIdentity() {
        return identity;
    }


    public Map<String, Object> getDetails() {
        return details;
    }

    public Object get(String key) {
        return details.getOrDefault(key, null);
    }
}
