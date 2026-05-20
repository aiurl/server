package io.theurl.framework.security;

@SuppressWarnings("unused")
public class CredentialNotFoundException extends CredentialException {
    public CredentialNotFoundException(Object credential) {
        super(credential);
    }

    public CredentialNotFoundException(Object credential, String message) {
        super(credential, message);
    }

    public CredentialNotFoundException(Object credential, String message, Throwable cause) {
        super(credential, message, cause);
    }
}
