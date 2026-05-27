package io.theurl.identity.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.theurl.identity.application.contract.AuthApplicationService;
import io.theurl.identity.application.dto.TokenGrantRequestDto;
import io.theurl.identity.application.dto.TokenGrantResponseDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final AuthApplicationService service;

    public AuthController(AuthApplicationService service) {
        this.service = service;
    }

    /**
     * Grant an access token based on the provided credentials.
     * This endpoint allows clients to obtain an access token by providing valid credentials, such as username and password.
     * The server will validate the credentials and, if they are correct, will issue an access token along with a refresh token.
     * The access token can be used for authentication and authorization in subsequent requests,
     * while the refresh token can be used to obtain a new access token when the current one expires.
     *
     * @param request The request containing the user's credentials.
     * @return A response containing the access token and refresh token.
     */
    @PostMapping("token/grant")
    @Operation(summary = "Grant access token")
    public TokenGrantResponseDto grantToken(@RequestBody TokenGrantRequestDto request) {
        return service.grant(request).join();
    }

    /**
     * Refresh the access token using the provided refresh token.
     * This endpoint allows clients to obtain a new access token when the current one expires, without requiring the user to re-authenticate.
     * The client must provide a valid refresh token, and if it is valid, a new access token will be issued along with a new refresh token.
     *
     * @param token The refresh token used to obtain a new access token.
     * @return A response containing the new access token and refresh token.
     */
    @PostMapping("token/refresh")
    @Operation(summary = "Refresh access token")
    public TokenGrantResponseDto refreshToken(@RequestParam String token) {
        var request = new TokenGrantRequestDto(token, null, "refresh_token", null);
        return service.grant(request).join();
    }

    /**
     * Revoke an access token based on the provided token identifier (jti).
     * This endpoint allows clients to revoke an access token, effectively invalidating it and preventing its further use for authentication and authorization.
     * The client must provide the token identifier (jti) of the access token to be revoked, and if the token is valid, it will be marked as revoked in the system.
     *
     * @param jti The token identifier (jti) of the access token to be revoked.
     */
    @PostMapping("token/revoke")
    @Operation(summary = "Revoke access token")
    public void revokeToken(@RequestParam String jti) {
        service.revoke(jti).join();
    }
}
