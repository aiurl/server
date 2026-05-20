package io.theurl.framework.security;

import java.util.Collections;
import java.util.Map;

/**
 * Base exception for credential-related errors, such as invalid credentials, expired credentials, etc.
 * This exception can be extended to provide more specific error types and include additional details as needed.
 */
@SuppressWarnings("unused")
public class CredentialException extends RuntimeException {
    private final String credential;
    private final Map<String, Object> details = Collections.emptyMap();


    public CredentialException(String credential) {
        this.credential = credential;
    }

    public CredentialException(String credential, String message) {
        super(message);
        this.credential = credential;
    }

    public CredentialException(String credential, String message, Throwable cause) {
        super(message, cause);
        this.credential = credential;
    }

    public String getCredential() {
        return credential;
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}
