package io.theurl.identity.interfaces.controller;

import io.theurl.identity.application.contract.OnetimePasswordApplicationService;
import io.theurl.identity.application.dto.OnetimePasswordSendRequestDto;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("otp")
public class OnetimePasswordController {
    private final OnetimePasswordApplicationService service;

    public OnetimePasswordController(OnetimePasswordApplicationService service) {
        this.service = service;
    }

    /**
     * Send a one-time password (OTP) to the specified recipient for authentication purposes.
     * This endpoint allows clients to request the sending of a one-time password (OTP) to a specified recipient, which can be used for authentication purposes.
     * The client must provide the recipient's information in the request body, and the server will process the request to generate and send the OTP to the recipient.
     * The server will return a response indicating the success or failure of the OTP sending operation, along with any relevant information such as the OTP code or an error message if the operation fails.
     *
     * @param request The request containing the recipient's information.
     * @return A CompletableFuture representing the asynchronous operation, containing the OTP code or an error message.
     */
    @PostMapping("authentication")
    public CompletableFuture<String> sendAuthOtp(@RequestBody OnetimePasswordSendRequestDto request) {
        return service.sendAsync(request.recipient(), "authentication");
    }

    /**
     * Send a one-time password (OTP) to the specified recipient for email change verification.
     * This endpoint allows clients to request the sending of a one-time password (OTP) to a specified recipient for the purpose of verifying an email change request.
     * The client must provide the recipient's information in the request body, and the server will process the request to generate and send the OTP to the recipient for email change verification.
     * The server will return a response indicating the success or failure of the OTP sending operation, along with any relevant information such as the OTP code or an error message if the operation fails.
     *
     * @param request The request containing the recipient's information.
     * @return A CompletableFuture representing the asynchronous operation, containing the OTP code or an error message.
     */
    @PostMapping("change-email")
    public CompletableFuture<String> sendChangeEmailOtp(@RequestBody OnetimePasswordSendRequestDto request) {
        return service.sendAsync(request.recipient(), "change-email");
    }

    /**
     * Send a one-time password (OTP) to the specified recipient for password reset verification.
     * This endpoint allows clients to request the sending of a one-time password (OTP) to a specified recipient for the purpose of verifying a password reset request.
     * The client must provide the recipient's information in the request body, and the server will process the request to generate and send the OTP to the recipient for password reset verification.
     * The server will return a response indicating the success or failure of the OTP sending operation, along with any relevant information such as the OTP code or an error message if the operation fails.
     *
     * @param request The request containing the recipient's information.
     * @return A CompletableFuture representing the asynchronous operation, containing the OTP code or an error message.
     */
    @PostMapping("reset-password")
    public CompletableFuture<String> sendResetPasswordOtp(@RequestBody OnetimePasswordSendRequestDto request) {
        return service.sendAsync(request.recipient(), "reset-password");
    }

    /**
     * Send a one-time password (OTP) to the specified recipient for a custom intent.
     * This endpoint allows clients to request the sending of a one-time password (OTP) to a specified recipient for a custom intent defined by the client.
     * The client must provide the recipient's information in the request body and specify the intent as a query parameter, and the server will process the request to generate and send the OTP to the recipient for the specified intent.
     * The server will return a response indicating the success or failure of the OTP sending operation, along with any relevant information such as the OTP code or an error message if the operation fails.
     *
     * @param request The request containing the recipient's information.
     * @param intent  The intent for which the OTP is being sent.
     * @return A CompletableFuture representing the asynchronous operation, containing the OTP code or an error message.
     */
    @PostMapping("send")
    public CompletableFuture<String> sendOtp(@RequestBody OnetimePasswordSendRequestDto request, @RequestParam String intent) {
        return service.sendAsync(request.recipient(), intent);
    }
}
