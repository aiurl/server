package io.theurl.framework.security;

@SuppressWarnings("unused")
public class CredentialExpiredException extends CredentialException {
    public CredentialExpiredException(Object credential) {
        super(credential);
    }

    public CredentialExpiredException(Object credential, String message) {
        super(credential, message);
    }

    public CredentialExpiredException(Object credential, String message, Throwable cause) {
        super(credential, message, cause);
    }
}
