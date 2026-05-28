package io.theurl.identity.application.contract;

import io.theurl.framework.application.ApplicationService;

import java.util.concurrent.CompletableFuture;

/**
 * Application service for handling one-time password (OTP) related operations, such as sending OTPs to users for various usages (e.g., authentication, password reset).
 */
public interface OnetimePasswordApplicationService extends ApplicationService {

    /**
     * Send a one-time password to the specified recipient for the given usage.
     *
     * @param recipient the recipient to whom the one-time password should be sent (e.g., email address, phone number)
     * @param usage the intended usage of the one-time password (e.g., "authentication", "password_reset")
     * @return a CompletableFuture that will complete with the sent OTP
     */
    CompletableFuture<String> sendAsync(String recipient, String usage);
}
