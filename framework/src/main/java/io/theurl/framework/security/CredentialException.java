package io.theurl.framework.security;

import java.util.Collections;
import java.util.Map;

/**
 * Base exception for credential-related errors, such as invalid credentials, expired credentials, etc.
 * This exception can be extended to provide more specific error types and include additional details as needed.
 */
@SuppressWarnings("unused")
public class CredentialException extends RuntimeException {
    private final Object credential;
    private final Map<String, Object> details = Collections.emptyMap();


    public CredentialException(Object credential) {
        this.credential = credential;
    }

    public CredentialException(Object credential, String message) {
        super(message);
        this.credential = credential;
    }

    public CredentialException(Object credential, String message, Throwable cause) {
        super(message, cause);
        this.credential = credential;
    }

    public Object getCredential() {
        return credential;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public Object get(String key) {
        return details.getOrDefault(key, null);
    }
}
