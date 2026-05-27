package io.theurl.identity.application.contract;

import io.theurl.framework.application.ApplicationService;
import io.theurl.identity.application.dto.OnetimePasswordSendRequestDto;

import java.util.concurrent.CompletableFuture;

/**
 * Application service for handling one-time password (OTP) related operations, such as sending OTPs to users for various usages (e.g., authentication, password reset).
 */
public interface OnetimePasswordApplicationService extends ApplicationService {

    /**
     * Send a one-time password to the specified recipient for the given usage.
     *
     * @param request the request containing the usage and recipient information
     * @return a CompletableFuture that will complete with the sent OTP
     */
    CompletableFuture<String> sendAsync(OnetimePasswordSendRequestDto request);
}
