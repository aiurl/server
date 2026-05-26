package io.theurl.identity.interfaces.controller;

import io.theurl.identity.application.contract.UserApplicationService;
import io.theurl.identity.application.dto.UserCreateRequestDto;
import io.theurl.identity.application.dto.UserPasswordChangeRequestDto;
import io.theurl.identity.application.dto.UserProfileResponseDto;
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
    public ResponseEntity<Void> create(@RequestBody UserCreateRequestDto user) {
        service.createAsync(user)
               .join();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseDto> getProfile() {
        var profileFuture = service.getProfileAsync();
        var profile = profileFuture.join();
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/password/change")
    public ResponseEntity<Void> changePassword(@RequestBody UserPasswordChangeRequestDto user) {
        service.changePasswordAsync(user.getOldPassword(), user.getNewPassword())
               .join();
        return ResponseEntity.ok().build();
    }

}
