package io.theurl.identity.interfaces.controller;

import io.theurl.identity.application.contract.UserApplicationService;
import io.theurl.identity.application.dto.UserCreateRequestDto;
import io.theurl.identity.application.dto.UserPasswordChangeRequestDto;
import io.theurl.identity.application.dto.UserProfileResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.theurl.identity.application.dto.UserUpdateRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account")
public class AccountController {
    private final UserApplicationService service;

    public AccountController(UserApplicationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<Void> create(@RequestBody UserCreateRequestDto user) {
        service.createAsync(user)
               .join();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    @Operation(summary = "Get current profile", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserProfileResponseDto> getProfile() {
        var profileFuture = service.getProfileAsync();
        var profile = profileFuture.join();
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/password/change")
    @Operation(summary = "Change current password", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> changePassword(@RequestBody UserPasswordChangeRequestDto user) {
        service.changePasswordAsync(user.getOldPassword(), user.getNewPassword())
               .join();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/email")
    @Operation(summary = "Change user email", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> changeEmail(@RequestBody UserUpdateRequestDto data) {
        service.changeEmailAsync(data.getEmail())
               .join();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/phone")
    @Operation(summary = "Change user phone", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> changePhone(@RequestBody UserUpdateRequestDto data) {
        service.changePhoneAsync(data.getPhone())
               .join();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/nickname")
    @Operation(summary = "Change user nickname", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> changeNickname(@RequestBody UserUpdateRequestDto data) {
        service.changeNicknameAsync(data.getNickname())
               .join();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authority/{provider}")
    @Operation(summary = "Bind OAuth authority to current user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> bindAuthority(@PathVariable String provider, @RequestParam String code) {
        service.connectAuthorityAsync(provider, code)
               .join();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/authority/{provider}")
    @Operation(summary = "Unbind OAuth authority from current user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> unbindAuthority(@PathVariable String provider, @RequestParam String openId) {
        service.removeAuthorityAsync(provider, openId)
               .join();
        return ResponseEntity.ok().build();
    }
}
