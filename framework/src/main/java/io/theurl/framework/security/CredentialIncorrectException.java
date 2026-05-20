package io.theurl.framework.security;

@SuppressWarnings("unused")
public class CredentialIncorrectException extends CredentialException {
    public CredentialIncorrectException(Object credential) {
        super(credential);
    }

    public CredentialIncorrectException(Object credential, String message) {
        super(credential, message);
    }

    public CredentialIncorrectException(Object credential, String message, Throwable cause) {
        super(credential, message, cause);
    }
}
